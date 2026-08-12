package com.project.expensemanager.service;

import com.project.expensemanager.entity.User;
import com.project.expensemanager.exception.UserExistsException;
import com.project.expensemanager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
    }

    public void register(User user, String rawPassword) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserExistsException("Username is already registered.");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserExistsException("Email is already registered.");
        }

        // Store hashed password
        user.setPasswordHash(passwordEncoder.encode(rawPassword));

        userRepository.save(user);
    }
}