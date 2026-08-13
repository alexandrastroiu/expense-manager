package com.project.expensemanager.dto.auth;

public record LoginRequest(
        String username,
        String password
) {
}