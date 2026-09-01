package com.project.expensemanager.service;

import com.project.expensemanager.entity.User;
import com.project.expensemanager.exception.UserExistsException;
import com.project.expensemanager.repository.UserRepository;
import com.project.expensemanager.security.JWTService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTService jwtService) {
            this.userRepository = userRepository;
            this.passwordEncoder = passwordEncoder;
            this.authenticationManager = authenticationManager;
            this.jwtService = jwtService;
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

    public String login(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return jwtService.generateToken(userDetails);
    }
}