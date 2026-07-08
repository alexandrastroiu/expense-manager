package com.project.expensemanager.service;

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
    
}
