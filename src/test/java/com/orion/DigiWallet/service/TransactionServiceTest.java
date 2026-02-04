package com.orion.DigiWallet.service;

import com.orion.DigiWallet.model.Category;
import com.orion.DigiWallet.model.Transaction;
import com.orion.DigiWallet.model.Wallet;
import com.orion.DigiWallet.repository.CategoryRepository;
import com.orion.DigiWallet.repository.TransactionRepository;
import com.orion.DigiWallet.repository.WalletRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionService Unit Tests")
class TransactionServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Wallet wallet;
    private Category incomeCategory;
    private Category expenseCategory;

    // ---------------------------------------------------------
    // COMMON OBJECT SETUP ONLY (NO STUBBING)
    // ---------------------------------------------------------
    @BeforeEach
    void setUp() {

        wallet = new Wallet();
        wallet.setId(1L);
        wallet.setBalance(BigDecimal.valueOf(1000));

        incomeCategory = new Category();
        incomeCategory.setId(10L);
        incomeCategory.setType("INCOME");

        expenseCategory = new Category();
        expenseCategory.setId(20L);
        expenseCategory.setType("EXPENSE");
    }

    // ---------------------------------------------------------
    @Nested
    @DisplayName("createTransaction()")
    //TODO: REMOVE @Disabled TO ENABLE THE TESTS
    //@Disabled
    class CreateTransactionTests {

        @Test
        @DisplayName("Given INCOME category when createTransaction then credit wallet")
        void givenIncomeCategory_whenCreateTransaction_thenCreditWallet() {

            // GIVEN
            Mockito.when(walletRepository.findById(1L))
                    .thenReturn(Optional.of(wallet));

            Mockito.when(categoryRepository.findById(10L))
                    .thenReturn(Optional.of(incomeCategory));

            Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // WHEN
            Transaction transaction =
                    transactionService.createTransaction(1L, 10L, 500.0);

            // THEN
            assertThat(wallet.getBalance()).isEqualByComparingTo("1500");
            assertThat(transaction.getTransactionType()).isEqualTo("CREDIT");
            assertThat(transaction.getReferenceId()).startsWith("TXN-");

            Mockito.verify(walletRepository).save(wallet);
        }

        @Test
        @DisplayName("Given EXPENSE category with sufficient balance when createTransaction then debit wallet")
        void givenExpenseCategoryWithSufficientBalance_whenCreateTransaction_thenDebitWallet() {

            // GIVEN
            Mockito.when(walletRepository.findById(1L))
                    .thenReturn(Optional.of(wallet));

            Mockito.when(categoryRepository.findById(20L))
                    .thenReturn(Optional.of(expenseCategory));

            Mockito.when(transactionRepository.save(Mockito.any(Transaction.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            // WHEN
            Transaction transaction =
                    transactionService.createTransaction(1L, 20L, 300.0);

            // THEN
            assertThat(wallet.getBalance()).isEqualByComparingTo("700");
            assertThat(transaction.getTransactionType()).isEqualTo("DEBIT");
        }

        @Test
        @DisplayName("Given EXPENSE category with insufficient balance when createTransaction then throw exception")
        void givenExpenseCategoryWithInsufficientBalance_whenCreateTransaction_thenThrowException() {

            // GIVEN
            wallet.setBalance(BigDecimal.valueOf(100));

            Mockito.when(walletRepository.findById(1L))
                    .thenReturn(Optional.of(wallet));

            Mockito.when(categoryRepository.findById(20L))
                    .thenReturn(Optional.of(expenseCategory));

            // WHEN / THEN
            assertThatThrownBy(() ->
                    transactionService.createTransaction(1L, 20L, 500.0))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Insufficient wallet balance");
        }

        @Test
        @DisplayName("Given invalid amount when createTransaction then throw exception")
        void givenInvalidAmount_whenCreateTransaction_thenThrowException() {

            // GIVEN
            Mockito.when(walletRepository.findById(1L))
                    .thenReturn(Optional.of(wallet));

            Mockito.when(categoryRepository.findById(10L))
                    .thenReturn(Optional.of(incomeCategory));

            // WHEN / THEN
            assertThatThrownBy(() ->
                    transactionService.createTransaction(1L, 10L, -100.0))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Amount must be greater than zero");
        }
    }

    // ---------------------------------------------------------
    @Nested
    @DisplayName("getTransactionById()")
    class GetTransactionByIdTests {

        @Test
        @DisplayName("Given existing transaction id when getTransactionById then return transaction")
        void givenTransactionId_whenGetTransactionById_thenReturnTransaction() {

            // GIVEN
            Mockito.when(transactionRepository.findById(1L))
                    .thenReturn(Optional.of(new Transaction()));

            // WHEN
            Transaction result = transactionService.getTransactionById(1L);

            // THEN
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Given non-existing transaction id when getTransactionById then throw exception")
        void givenInvalidTransactionId_whenGetTransactionById_thenThrowException() {

            // GIVEN
            Mockito.when(transactionRepository.findById(1L))
                    .thenReturn(Optional.empty());

            // WHEN / THEN
            assertThatThrownBy(() ->
                    transactionService.getTransactionById(1L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Transaction not found");
        }
    }

    // ---------------------------------------------------------
    @Nested
    @DisplayName("getTransactionsByUserId()")
    class GetTransactionsByUserIdTests {

        @Test
        @DisplayName("Given user id when getTransactionsByUserId then return transactions")
        void givenUserId_whenGetTransactionsByUserId_thenReturnTransactions() {

            // GIVEN
            Mockito.when(transactionRepository.findByWallet_User_Id(5L))
                    .thenReturn(List.of(new Transaction(), new Transaction()));

            // WHEN
            List<Transaction> result =
                    transactionService.getTransactionsByUserId(5L);

            // THEN
            assertThat(result).hasSize(2);
        }
    }
}
