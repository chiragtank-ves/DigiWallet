package com.orion.DigiWallet.service;

import com.orion.DigiWallet.model.User;
import com.orion.DigiWallet.repository.UserRepository;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService  {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users from database");

        List<User> users = userRepository.findAll();
        logger.info("Total users fetched: {}", users.size());
        for (User user : users) {
            String greeting = generateGreetingMsg(user.getRole());
            user.setUserGreetingMessage(greeting);
        }
        return users;
    }

    public User getUserById(Long id) {

        logger.info("Fetching user with id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        String greeting = generateGreetingMsg(user.getRole());
        user.setUserGreetingMessage(greeting);
        return user;

    }

    @Transactional
    public User createUser(User user) {
        logger.info("Creating new user with username {}", user.getUsername());
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        User savedUser = userRepository.save(user);
        logger.info("User created successfully with id {}", savedUser.getId());
        return savedUser;
    }

    String generateGreetingMsg(String role) {

        boolean isAdmin = role != null && role.equalsIgnoreCase("ADMIN");
        if (isAdmin) {
            return "Hello ADMIN - Admin access enabled";
        }
        return "Hello USER - User access";
    }

    public User updateUserStatus(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
            user.setStatus("INACTIVE");
        } else {
            user.setStatus("ACTIVE");
        }

        User updatedUser = userRepository.save(user);
        logger.info("User status updated successfully for id {}", updatedUser.getId());

        return updatedUser;
    }

    //TODO: 1.5
    // READ ONLY
    // Simple Calculator methods add, subtract, multiply, divide
    // Go through the basic operations and implementations
    public int add(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero is not allowed.");
        }
        return a / b;
    }
}
