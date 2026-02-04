package com.orion.DigiWallet.service;

import com.orion.DigiWallet.model.User;
import com.orion.DigiWallet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository mockUserRepository;


    @InjectMocks
    UserService userService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setFullName("Chirag Tank");
        user1.setRole("USER");

        user2 = new User();
        user2.setId(2L);
        user2.setFullName("Admin User");
        user2.setRole("ADMIN");
    }

    //TODO: 1.6 - calculator method tests
    // READ ONLY
    // RUN THIS TEST AND UNDERSTAND THE WORKING
    // disucss what is nested class
    // what is @test annotation
    // what is assertEquals
    // what is assertThrows
    // what is IllegalArgumentException

    @Nested
            // @nested annotation is used to group related test cases together
    class CalculatorTests {

        @Test
        void add_shouldReturnSum_whenTwoPositiveNumbersProvided() {
            int result = userService.add(5, 3);
            assertEquals(8, result);
        }

        @Test
        void add_shouldReturnSum_whenNegativeNumbersProvided() {
            int result = userService.add(-4, -6);
            assertEquals(-10, result);
        }

        @Test
        void subtract_shouldReturnDifference_whenValidNumbersProvided() {
            int result = userService.subtract(10, 4);
            assertEquals(6, result);
        }

        @Test
        void subtract_shouldReturnNegativeResult_whenSecondNumberIsGreater() {
            int result = userService.subtract(3, 7);
            assertEquals(-4, result);
        }

        @Test
        void multiply_shouldReturnProduct_whenValidNumbersProvided() {
            int result = userService.multiply(4, 5);
            assertEquals(20, result);
        }

        @Test
        void multiply_shouldReturnZero_whenOneOperandIsZero() {
            int result = userService.multiply(7, 0);
            assertEquals(0, result);
        }

        @Test
        void divide_shouldReturnQuotient_whenValidNumbersProvided() {
            int result = userService.divide(20, 4);
            assertEquals(5, result);
        }

        @Test
        void divide_shouldThrowException_whenDividingByZero() {
            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class,
                            () -> userService.divide(10, 0));

            assertEquals("Division by zero is not allowed.", exception.getMessage());
        }
    }


    @Nested
    class GenerateGreetingMsgTests {
        @Test
        void shouldReturnAdminGreeting_whenRoleIsAdmin() {
            String result = userService.generateGreetingMsg("ADMIN");
            assertEquals("Hello ADMIN - Admin access enabled", result);
        }

        @Test
        void shouldReturnUserGreeting_whenRoleIsNotAdmin() {
            String result = userService.generateGreetingMsg("USER");
            assertEquals("Hello USER - User access", result);
        }

        @Test
        void shouldReturnUserGreeting_whenRoleIsNull() {
            String result = userService.generateGreetingMsg(null);
            assertEquals("Hello USER - User access", result);
        }
    }

    //TODO: 1.8.0
    // READ-ONLY
    // RUN THIS TEST AND UNDERSTAND THE WORKING
    @Test
    void getAllUsers_shouldReturnUsersWithGreetingMessage() {

        // GIVEN
        List<User> mockUsers = Arrays.asList(user1, user2);
        when(mockUserRepository.findAll()).thenReturn(mockUsers);

        // WHEN
        List<User> result = userService.getAllUsers();

        // THEN
        assertEquals(2, result.size());

        //DONE: 1.8.1
        // Verify greeting message is generated
        // Uncomment the below assertions after implementing greeting message logic
        //ONLY IF 1.4 IS DONE

        assertNotNull(result.get(0).getUserGreetingMessage());
        assertNotNull(result.get(1).getUserGreetingMessage());

        assertTrue(result.get(0).getUserGreetingMessage().contains("User access"));
        assertTrue(result.get(1).getUserGreetingMessage().contains("Admin access"));

        // Verify repository interaction
        verify(mockUserRepository, times(1)).findAll();
    }

    @Test
    void getUserById_shouldReturnUserWithGreetingMessage() {
        // GIVEN
        when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(user1));

        // WHEN
        User result = userService.getUserById(1L);

        // THEN
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getUserGreetingMessage());
        assertTrue(result.getUserGreetingMessage().contains("User access"));

        verify(mockUserRepository).findById(1L);
    }

    //TODO: 1.10
    // READ ONLY
    // implement the unit test for createUser method in UserService
    @Nested
    class CreateUserTests {

        @Test
        void createUser_shouldSaveUser_whenUsernameDoesNotExist() {

            // GIVEN
            User inputUser = new User();
            inputUser.setUsername("newuser");

            User savedUser = new User();
            savedUser.setId(1L);
            savedUser.setUsername("newuser");

            when(mockUserRepository.existsByUsername("newuser"))
                    .thenReturn(false);

            when(mockUserRepository.save(any(User.class)))
                    .thenReturn(savedUser);

            // WHEN
            User result = userService.createUser(inputUser);

            // THEN
            assertNotNull(result);
            assertEquals(1L, result.getId());

            verify(mockUserRepository).existsByUsername("newuser");
            verify(mockUserRepository).save(any(User.class));
        }

        @Test
        void createUser_shouldThrowException_whenUsernameExists() {

            // GIVEN
            User inputUser = new User();
            inputUser.setUsername("existinguser");

            when(mockUserRepository.existsByUsername("existinguser"))
                    .thenReturn(true);

            // WHEN + THEN
            assertThrows(RuntimeException.class,
                    () -> userService.createUser(inputUser));

            verify(mockUserRepository).existsByUsername("existinguser");
            verify(mockUserRepository, never()).save(any(User.class));
        }
    }

    @Nested
    class UpdateUserStatusTests {

        @Test
        void updateUserStatus_shouldSetToInactive_whenCurrentStatusIsActive() {
            // GIVEN
            User activeUser = new User();
            activeUser.setId(1L);
            activeUser.setUsername("testuser");
            activeUser.setStatus("ACTIVE");

            User updatedUser = new User();
            updatedUser.setId(1L);
            updatedUser.setUsername("testuser");
            updatedUser.setStatus("INACTIVE");

            when(mockUserRepository.findById(1L))
                    .thenReturn(Optional.of(activeUser));

            when(mockUserRepository.save(any(User.class)))
                    .thenReturn(updatedUser);

            // WHEN
            User result = userService.updateUserStatus(1L);

            // THEN
            assertNotNull(result);
            assertEquals("INACTIVE", result.getStatus());

            verify(mockUserRepository).findById(1L);
            verify(mockUserRepository).save(any(User.class));
        }

        @Test
        void updateUserStatus_shouldSetToActive_whenCurrentStatusIsInactive() {
            // GIVEN
            User inactiveUser = new User();
            inactiveUser.setId(2L);
            inactiveUser.setUsername("testuser2");
            inactiveUser.setStatus("INACTIVE");

            User updatedUser = new User();
            updatedUser.setId(2L);
            updatedUser.setUsername("testuser2");
            updatedUser.setStatus("ACTIVE");

            when(mockUserRepository.findById(2L))
                    .thenReturn(Optional.of(inactiveUser));

            when(mockUserRepository.save(any(User.class)))
                    .thenReturn(updatedUser);

            // WHEN
            User result = userService.updateUserStatus(2L);

            // THEN
            assertNotNull(result);
            assertEquals("ACTIVE", result.getStatus());

            verify(mockUserRepository).findById(2L);
            verify(mockUserRepository).save(any(User.class));
        }

        @Test
        void updateUserStatus_shouldSetToActive_whenCurrentStatusIsNull() {
            // GIVEN
            User userWithNullStatus = new User();
            userWithNullStatus.setId(3L);
            userWithNullStatus.setUsername("testuser3");
            userWithNullStatus.setStatus(null);

            User updatedUser = new User();
            updatedUser.setId(3L);
            updatedUser.setUsername("testuser3");
            updatedUser.setStatus("ACTIVE");

            when(mockUserRepository.findById(3L))
                    .thenReturn(Optional.of(userWithNullStatus));

            when(mockUserRepository.save(any(User.class)))
                    .thenReturn(updatedUser);

            // WHEN
            User result = userService.updateUserStatus(3L);

            // THEN
            assertNotNull(result);
            assertEquals("ACTIVE", result.getStatus());

            verify(mockUserRepository,times(1)).findById(3L);
            verify(mockUserRepository,times(1)).save(any(User.class));
        }

        @Test
        void updateUserStatus_shouldThrowException_whenUserNotFound() {
            // GIVEN
            Long nonExistentId = 999L;

            when(mockUserRepository.findById(nonExistentId))
                    .thenReturn(Optional.empty());

            // WHEN + THEN
            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> userService.updateUserStatus(nonExistentId));

            assertEquals("User not found with id " + nonExistentId, exception.getMessage());

            verify(mockUserRepository,times(1)).findById(nonExistentId);
            verify(mockUserRepository, never()).save(any(User.class));
        }
    }
}
