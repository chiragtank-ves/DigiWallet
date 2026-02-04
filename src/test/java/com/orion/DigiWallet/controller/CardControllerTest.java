package com.orion.DigiWallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.DigiWallet.model.Card;
import com.orion.DigiWallet.model.Wallet;
import com.orion.DigiWallet.service.CardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//DONE: 4.5.1
// Create unit tests for CardController
// covering all CRUD operations
// Use MockMvc and Mockito for testing
// USE proper annotations and structure similar to UserControllerTest and WalletControllerTest
@WebMvcTest(CardController.class)
class CardControllerTest {


    //DONE: 4.5.2
    // create private variables for MockMvc, CardService and ObjectMapper
    // use @Autowired for MockMvc and ObjectMapper
    // use @MockitoBean for CardService
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("POST /api/cards/create")
    class CreateCardTests {

        @Test
        @DisplayName("Given valid card with wallet when createCard then return created card")
        void givenValidCardWithWallet_whenCreateCard_thenReturnCreatedCard() throws Exception {

            //DONE: 4.5.3
            // GIVEN
            Card inputCard = new Card();
            Wallet wallet = new Wallet();
            wallet.setId(1L);
            inputCard.setWallet(wallet);
            inputCard.setCardNumber("1234567890123456");
            inputCard.setCardType("DEBIT");
            inputCard.setStatus("ACTIVE");

            Card savedCard = new Card();
            savedCard.setId(1L);
            savedCard.setWallet(wallet);
            savedCard.setCardNumber("1234567890123456");
            savedCard.setCardType("DEBIT");
            savedCard.setStatus("ACTIVE");
            savedCard.setIssuedAt(LocalDateTime.now());

            Mockito.when(cardService.createCard(Mockito.any(Card.class))).thenReturn(savedCard);

            // WHEN & THEN
            mockMvc.perform(post("/api/cards/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputCard)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.cardNumber").value("1234567890123456"))
                    .andExpect(jsonPath("$.cardType").value("DEBIT"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"));
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("GET /api/cards/{id}")
    class GetCardByIdTests {

        @Test
        @DisplayName("Given existing card id when getCardById then return card")
        void givenExistingCardId_whenGetCardById_thenReturnCard() throws Exception {
            //DONE: 4.5.4
            // GIVEN
            Card card = new Card();
            card.setId(1L);
            Wallet wallet = new Wallet();
            wallet.setId(1L);
            card.setWallet(wallet);
            card.setCardNumber("1234567890123456");
            card.setCardType("DEBIT");
            card.setStatus("ACTIVE");
            card.setIssuedAt(LocalDateTime.now());

            Mockito.when(cardService.getCardById(1L)).thenReturn(card);

            // WHEN & THEN
            mockMvc.perform(get("/api/cards/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.cardNumber").value("1234567890123456"))
                    .andExpect(jsonPath("$.cardType").value("DEBIT"));
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("PUT /api/cards/{id}")
    class UpdateCardTests {

        @Test
        @DisplayName("Given valid update request when updateCard then return updated card")
        void givenValidUpdate_whenUpdateCard_thenReturnUpdatedCard() throws Exception {
            //DONE: 4.5.5
            // GIVEN
            Card updateRequest = new Card();
            updateRequest.setStatus("BLOCKED");

            Card updatedCard = new Card();
            updatedCard.setId(1L);
            Wallet wallet = new Wallet();
            wallet.setId(1L);
            updatedCard.setWallet(wallet);
            updatedCard.setCardNumber("1234567890123456");
            updatedCard.setCardType("DEBIT");
            updatedCard.setStatus("BLOCKED");
            updatedCard.setIssuedAt(LocalDateTime.now());

            Mockito.when(cardService.updateCard(Mockito.eq(1L), Mockito.any(Card.class))).thenReturn(updatedCard);

            // WHEN & THEN
            mockMvc.perform(put("/api/cards/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.status").value("BLOCKED"));
        }
    }

    // ---------------------------------------------------------------------
    @Nested
    @DisplayName("DELETE /api/cards/{id}")
    class DeleteCardTests {

        @Test
        @DisplayName("Given card id when deleteCard then return success message")
        void givenCardId_whenDeleteCard_thenReturnSuccessMessage() throws Exception {
            //DONE: 4.5.6
            // GIVEN
            Mockito.doNothing().when(cardService).deleteCard(1L);

            // WHEN & THEN
            mockMvc.perform(delete("/api/cards/1"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Card deleted successfully"));
        }
    }
}
