package com.orion.DigiWallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.DigiWallet.model.Category;
import com.orion.DigiWallet.model.Transaction;
import com.orion.DigiWallet.model.Wallet;
import com.orion.DigiWallet.service.TransactionService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@DisplayName("TransactionController Unit Tests")
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    // ------------------------------------------------------------------
    //@Disabled
    @Nested
    @DisplayName("POST /api/transactions/create")
    class CreateTransactionTests {

        @Test
        @DisplayName("Given valid params when creditAmount then return created transaction")
        void givenValidParams_whenCreditAmount_thenReturnTransaction() throws Exception {

            // GIVEN
            Wallet wallet = new Wallet();
            wallet.setId(1L);
            wallet.setBalance(BigDecimal.valueOf(1000));


            Category category = new Category();
            category.setId(8L);

            Transaction transaction = new Transaction();
            transaction.setWallet(wallet);
            transaction.setCategory(category);
            transaction.setAmount(1000.0);
            transaction.setTransactionType("CREDIT");
            transaction.setReferenceId("TXN-001");

            Mockito.when(transactionService.createTransaction(1L, 8L, 1000.0))
                    .thenReturn(transaction);

            // WHEN & THEN
            mockMvc.perform(post("/api/transactions/create")
                            .param("walletId", "1")
                            .param("categoryId", "8")
                            .param("amount", "1000"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amount").value(1000.0))
                    .andExpect(jsonPath("$.transactionType").value("CREDIT"))
                    .andExpect(jsonPath("$.wallet.id").value(1L))
                    .andExpect(jsonPath("$.category.id").value(8L));
        }
    }

    // ------------------------------------------------------------------
    @Nested
    @DisplayName("GET /api/transactions/{id}")
    class GetTransactionByIdTests {

        @Test
        @DisplayName("Given existing transaction id when getTransactionById then return transaction")
        void givenTransactionId_whenGetTransactionById_thenReturnTransaction() throws Exception {

            // GIVEN
            Wallet wallet = new Wallet();
            wallet.setId(2L);

            Transaction transaction = new Transaction();
            transaction.setWallet(wallet);
            transaction.setAmount(500.0);
            transaction.setTransactionType("DEBIT");

            Mockito.when(transactionService.getTransactionById(10L))
                    .thenReturn(transaction);

            // WHEN & THEN
            mockMvc.perform(get("/api/transactions/{id}", 10L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.amount").value(500.0))
                    .andExpect(jsonPath("$.transactionType").value("DEBIT"))
                    .andExpect(jsonPath("$.wallet.id").value(2L));
        }
    }

    // ------------------------------------------------------------------
    @Nested
    @DisplayName("GET /api/transactions/user/{userId}")
    class GetTransactionsByUserIdTests {

        @Test
        @DisplayName("Given user id when getTransactionsByUserId then return list of transactions")
        void givenUserId_whenGetTransactionsByUserId_thenReturnTransactionList() throws Exception {

            // GIVEN
            Transaction t1 = new Transaction();
            t1.setAmount(200.0);
            t1.setTransactionType("CREDIT");

            Transaction t2 = new Transaction();
            t2.setAmount(300.0);
            t2.setTransactionType("DEBIT");

            Mockito.when(transactionService.getTransactionsByUserId(5L))
                    .thenReturn(List.of(t1, t2));

            // WHEN & THEN
            mockMvc.perform(get("/api/transactions/user/{userId}", 5L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].transactionType").value("CREDIT"))
                    .andExpect(jsonPath("$[1].transactionType").value("DEBIT"));
        }
    }
}
