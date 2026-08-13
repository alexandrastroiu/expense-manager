package com.project.expensemanager.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotNull(message = "Username is required for login")
        @Size(max = 50, message = "Username cannot exceed 50 characters")
        String username,

        @NotNull(message = "Password is required for login")
        @Size(max = 255, message = "Password cannot exceed 255 characters")
        String password
) {
}