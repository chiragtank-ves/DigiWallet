package com.orion.DigiWallet.repository;


import com.orion.DigiWallet.model.User;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
// H2 in-memory database will be auto-configured by @DataJpaTest
// FIRST SEE THE APPLICATION.PROPERTIES IN TEST RESOURCES FOLDER
    // ALSO LOOK AT THE DBSCIPT.SQL AND DATAINSERT.SQL FILES IN MAIN FOLDER
//DONE: REMOVE @Disabled TO ENABLE THE TESTS
//@Disabled
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    //DONE: REMOVE @Disabled TO ENABLE THE TEST
    //@Disabled
    @Test
    void existsByUsername_shouldReturnTrue_whenUserExists() {

        // GIVEN
        User user = new User();
        user.setUsername("AlexTest");
        user.setFullName("Alex Johnson");
        user.setRole("USER");
        user.setStatus("ACTIVE");

        userRepository.save(user);

        // WHEN
        boolean exists = userRepository.existsByUsername("AlexTest");

        // THEN
        assertThat(exists).isTrue();
    }
    //DONE: REMOVE @Disabled TO ENABLE THE TEST
    //@Disabled
    @Test
    void existsByUsername_shouldReturnFalse_whenUserDoesNotExist() {

        // WHEN
        boolean exists = userRepository.existsByUsername("unknown");

        // THEN
        assertThat(exists).isFalse();
    }
}

