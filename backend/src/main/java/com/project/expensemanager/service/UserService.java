package com.project.expensemanager.service;

import com.project.expensemanager.entity.User;
import com.project.expensemanager.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Inject repositories
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Business logic
    User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow( () -> new RuntimeException("User with username " + username + " not found."));
    }

    User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow( () -> new RuntimeException("User with email " + email + " not found"));
    }
}
