package com.project.expensemanager.mapper;

import com.project.expensemanager.dto.auth.RegisterRequest;
import com.project.expensemanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    // Map request to entity
    public User mapToEntity(RegisterRequest request) {
        return new User(request.username(), request.firstName(), request.lastName(), request.email());
    }
}
