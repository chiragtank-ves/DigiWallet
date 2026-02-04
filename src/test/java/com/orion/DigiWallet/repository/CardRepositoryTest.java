package com.orion.DigiWallet.repository;

import com.orion.DigiWallet.model.Card;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
// FIRST SEE THE APPLICATION.PROPERTIES IN TEST RESOURCES FOLDER
// ALSO LOOK AT THE DBSCIPT.SQL AND DATAINSERT.SQL FILES IN MAIN FOLDER
//RUN THE SHELL SCRIPT TO CREATE THE TABLES IN TEST DATABASE BEFORE RUNNING THE TESTS
//TODO: 3.5.1: REMOVE @Disabled TO ENABLE THE TESTS
@Disabled
public class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    private Wallet wallet;

    //TODO: 3.5.2: REVIEW SETUP METHOD
    @BeforeEach
    void setUp() {
        // GIVEN
        // Create and save a User (required for Wallet)
        User user = new User();
        user.setUsername("Test User");
        user.setEmail("test.user@example.com");
        user = userRepository.save(user);

        // Create and save a Wallet linked to the user
        Wallet w = new Wallet();
        w.setUser(user);
        w.setBalance(BigDecimal.ZERO);
        w.setCurrency("INR");
        w.setStatus("ACTIVE");

        this.wallet = walletRepository.save(w);
    }

    //TODO: 3.5.3:
    // Write a test to verify:
    // - A Card can be saved successfully
    // - Card ID is generated
    //@Disabled
    @Test
    void shouldSaveCardSuccessfully() {
        // GIVEN


        // WHEN


        // THEN

    }

    //TODO: 3.5.4:
    // Write a test to verify:
    // - Card can be fetched by cardNumber
    @Test
   // @Disabled
    void shouldFindCardByCardNumber() {
        // GIVEN


        // WHEN


        // THEN

    }

    //TODO: 3.5.5
    // Write a test to verify:
    // - existsByCardNumber returns true for existing card
    // - existsByCardNumber returns false for non-existing card
    //@Disabled
    @Test
    void shouldCheckIfCardExistsByCardNumber() {
        // GIVEN


        // WHEN + THEN

    }
}
