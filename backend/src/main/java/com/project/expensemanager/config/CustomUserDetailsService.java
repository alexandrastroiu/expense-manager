package com.project.expensemanager.config;

import com.project.expensemanager.entity.User;
import com.project.expensemanager.exception.ResourceNotFoundException;
import com.project.expensemanager.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
            // Fetch user from the database
            User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found."));

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPasswordHash())
                    .authorities("USER")
                    .build();
    }
}