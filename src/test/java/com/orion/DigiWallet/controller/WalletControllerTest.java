package com.orion.DigiWallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.DigiWallet.model.User;
import com.orion.DigiWallet.model.Wallet;
import com.orion.DigiWallet.service.WalletService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WalletController.class)
@DisplayName("WalletController Unit Tests")

//DONE: 4.3: Enable tests after implementing 4.3 in WalletController
// REMOVE the @Disabled annotation to run the tests
// Uncomment test code after implementing the WalletController methods
// and ensure all tests pass
//@Disabled
class WalletControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    // ------------------------------------------------------------------
    @Nested
    @DisplayName("GET /api/wallets/{id}")
    class GetWalletByIdTests {

        @Test
        @DisplayName("Given existing wallet id when getWalletById then return wallet")
        void givenWalletId_whenGetWalletById_thenReturnWallet() throws Exception {

            // GIVEN
            User user = new User();
            user.setId(8L);

            Wallet wallet = new Wallet();
            wallet.setId(1L);
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.valueOf(5000));
            wallet.setCurrency("INR");
            wallet.setStatus("ACTIVE");

            Mockito.when(walletService.getWalletById(1L))
                    .thenReturn(wallet);

            // WHEN & THEN
            mockMvc.perform(get("/api/wallets/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.user.id").value(8L))
                    .andExpect(jsonPath("$.balance").value(5000))
                    .andExpect(jsonPath("$.currency").value("INR"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"));
        }
    }

    // ------------------------------------------------------------------
    @Nested
    @DisplayName("GET /api/wallets/user/{userId}")
    class GetWalletByUserIdTests {

        @Test
        @DisplayName("Given existing user id when getWalletByUserId then return wallet")
        void givenUserId_whenGetWalletByUserId_thenReturnWallet() throws Exception {

            // GIVEN
            User user = new User();
            user.setId(42L);

            Wallet wallet = new Wallet();
            wallet.setId(10L);
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.valueOf(2500));
            wallet.setCurrency("INR");
            wallet.setStatus("ACTIVE");

            Mockito.when(walletService.getWalletByUserId(42L))
                    .thenReturn(wallet);

            // WHEN & THEN
            mockMvc.perform(get("/api/wallets/user/{userId}", 42L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(10L))
                    .andExpect(jsonPath("$.user.id").value(42L))
                    .andExpect(jsonPath("$.balance").value(2500))
                    .andExpect(jsonPath("$.currency").value("INR"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"));
        }

        @Test
        @DisplayName("Given non-existing user id when getWalletByUserId then return server error")
        void givenNonExistingUserId_whenGetWalletByUserId_thenReturnServerError() throws Exception {

            // GIVEN
            Mockito.when(walletService.getWalletByUserId(999L))
                    .thenThrow(new RuntimeException("Wallet not found for user"));

            // WHEN & THEN
            mockMvc.perform(get("/api/wallets/user/{userId}", 999L))
                    .andExpect(status().is5xxServerError());
        }
    }

    // ------------------------------------------------------------------
    @Nested
    @DisplayName("POST /api/wallets")
    class CreateWalletTests {

        @Test
        @DisplayName("Given valid wallet request when createWallet then return created wallet")
        void givenValidWallet_whenCreateWallet_thenReturnCreatedWallet() throws Exception {

            // GIVEN
            User user = new User();
            user.setId(8L);

            Wallet wallet = new Wallet();
            wallet.setId(2L);
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.valueOf(5000));
            wallet.setCurrency("INR");
            wallet.setStatus("ACTIVE");

            Mockito.when(walletService.createWallet(Mockito.any(Wallet.class)))
                    .thenReturn(wallet);

            // WHEN & THEN
            mockMvc.perform(post("/api/wallets")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(wallet)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(2L))
                    .andExpect(jsonPath("$.user.id").value(8L))
                    .andExpect(jsonPath("$.balance").value(5000))
                    .andExpect(jsonPath("$.currency").value("INR"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"));
        }
    }
}
