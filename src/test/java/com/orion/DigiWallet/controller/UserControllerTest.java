package com.orion.DigiWallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orion.DigiWallet.model.User;
import com.orion.DigiWallet.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User user1;
    private User user2;

    // ---------------------------------------------
    // Common Test Setup
    // ---------------------------------------------
    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("john");
        user1.setStatus("ACTIVE");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("mary");
        user2.setStatus("ACTIVE");
    }

    // ---------------------------------------------
    // GET /api/users
    // ---------------------------------------------
    @Nested
    class GetAllUsersTests {

        @Test
        void getAllUsers_shouldReturnListOfUsers_whenUsersExist() throws Exception {
            // GIVEN
            Mockito.when(userService.getAllUsers())
                    .thenReturn(List.of(user1, user2));

            // WHEN & THEN
            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2));
        }

        @Test
        void getAllUsers_shouldReturnEmptyList_whenNoUsersExist() throws Exception {
            // GIVEN
            Mockito.when(userService.getAllUsers())
                    .thenReturn(List.of());

            // WHEN & THEN
            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(0));
        }
    }

    // ---------------------------------------------
    // GET /api/users/{id}
    // ---------------------------------------------
    @Nested
    class GetUserByIdTests {

        @Test
        void getUserById_shouldReturnUser_whenValidIdProvided() throws Exception {
            // GIVEN
            Mockito.when(userService.getUserById(1L))
                    .thenReturn(user1);

            // WHEN & THEN
            mockMvc.perform(get("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("john"));
        }

        @Test
        void getUserById_shouldReturnError_whenUserNotFound() throws Exception {
            // GIVEN
            Mockito.when(userService.getUserById(99L))
                    .thenThrow(new RuntimeException("User not found"));

            // WHEN & THEN
            mockMvc.perform(get("/api/users/99"))
                    .andExpect(status().isInternalServerError());
        }
    }

    // ---------------------------------------------
    // POST /api/users
    // ---------------------------------------------
@Nested
class CreateUserTests {

        @Test
        void createUser_shouldReturnCreatedUser_whenValidInputProvided() throws Exception {
            // GIVEN
            User inputUser = new User();
            inputUser.setUsername("john");
            inputUser.setStatus("ACTIVE");

            Mockito.when(userService.createUser(Mockito.any(User.class)))
                    .thenReturn(user1);

            // WHEN & THEN
            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L));
        }

        @Test
        void createUser_shouldReturnError_whenServiceThrowsException() throws Exception {
            // GIVEN
            User inputUser = new User();
            inputUser.setUsername("john");
            inputUser.setStatus("ACTIVE");

            Mockito.when(userService.createUser(Mockito.any(User.class)))
                    .thenThrow(new RuntimeException("Username already exists"));

            // WHEN & THEN
            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputUser)))
                    .andExpect(status().isInternalServerError());
        }
    }

    // ---------------------------------------------
    // PUT /api/users/{id}
    // ---------------------------------------------
    @Nested
    class UpdateUserStatusTests {

        @Test
        void updateUserStatus_shouldToggleStatus_whenValidUserIdProvided() throws Exception {
            // GIVEN
            user1.setStatus("INACTIVE");

            Mockito.when(userService.updateUserStatus(1L))
                    .thenReturn(user1);

            // WHEN & THEN
            mockMvc.perform(put("/api/users/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("INACTIVE"));
        }

        @Test
        void updateUserStatus_shouldReturnError_whenUserDoesNotExist() throws Exception {
            // GIVEN
            Mockito.when(userService.updateUserStatus(99L))
                    .thenThrow(new RuntimeException("User not found"));

            // WHEN & THEN
            mockMvc.perform(put("/api/users/99"))
                    .andExpect(status().isInternalServerError());
        }
    }
}
