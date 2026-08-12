package com.project.expensemanager.controller;

import com.project.expensemanager.dto.auth.RegisterRequest;
import com.project.expensemanager.entity.User;
import com.project.expensemanager.mapper.UserMapper;
import com.project.expensemanager.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserMapper userMapper;
    private final AuthService authService;

    private AuthController(AuthService service, UserMapper userMapper) {
        this.userMapper = userMapper;
        this.authService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        User user = userMapper.mapToEntity(request);

        authService.register(user, request.password());

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}