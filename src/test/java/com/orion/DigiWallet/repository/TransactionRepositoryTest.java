package com.orion.DigiWallet.repository;

import com.orion.DigiWallet.model.Category;
import com.orion.DigiWallet.model.Transaction;
import com.orion.DigiWallet.model.User;
import com.orion.DigiWallet.model.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
// FIRST SEE THE APPLICATION.PROPERTIES IN TEST RESOURCES FOLDER
// ALSO LOOK AT THE DBSCIPT.SQL AND DATAINSERT.SQL FILES IN MAIN FOLDER
//RUN THE SHELL SCRIPT TO CREATE THE TABLES IN TEST DATABASE BEFORE RUNNING THE TESTS
//TODO: 3.6.1: REMOVE @Disabled TO ENABLE THE TESTS
@Disabled
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private User user;
    private Wallet wallet;
    private Category category;

    @BeforeEach
    void setUp() {
        // GIVEN
        // Create and persist User
        //TODO: 3.6.2: SET OTHER MANDATORY FIELDS IF ANY IN USER ENTITY
        // create user with dummy data eg username, email etc
        // username: "Test User", email: "txn.user@test.com
        // Save the user to the repository and assign to this.user


        // Create and persist Wallet linked to User
        //TODO: 3.6.3: Create wallet with dummy data eg balance, currency, status etc
        // balance: 1000, currency: "INR", status: "ACTIVE"
        // Link the wallet to the user created above
        // Balance is BigDecimal.valueOf(1000)
        // Save the wallet to the repository and assign to this.wallet

        // Create and persist Category
        //TODO: 3.6.4: Create category with type "EXPENSE"
        // Save the category to the repository and assign to this.category

    }

    //TODO: 3.6.5: READ ONLY
    // Write a test to verify:
    // - Transaction can be saved successfully
    // - Transaction ID is generated
   // @Disabled
    @Test
    void shouldSaveTransactionSuccessfully() {
        // GIVEN
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setCategory(category);
        transaction.setAmount(250.0);
        transaction.setTransactionType("DEBIT");
        transaction.setReferenceId("TXN-TEST-001");

        // WHEN
        Transaction savedTransaction = transactionRepository.save(transaction);

        // THEN
        assertThat(savedTransaction.getId()).isNotNull();
    }

    // TODO: 3.6.6:
    // Write a test to verify:
    // - Transactions can be fetched by User ID
   // @Disabled
    @Test
    void shouldFindTransactionsByUserId() {
        // GIVEN
        // Create and save multiple Transactions for the Wallet


        // WHEN


        // THEN

    }

    // TODO: 3.6.7
    // Write a test to verify:
    // - Empty list is returned when no transactions exist for user
    //@Disabled
    @Test
    void shouldReturnEmptyListWhenNoTransactionsForUser() {
        // GIVEN

        // WHEN

        // THEN

    }
}
