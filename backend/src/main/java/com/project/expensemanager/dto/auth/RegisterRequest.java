package com.project.expensemanager.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotNull(message = "Username is required")
        @Size(max = 50, message = "Username cannot exceed 50 characters")
        String username,

        @NotNull(message = "First name is required")
        @Size(max = 50, message = "First name cannot exceed 50 characters")
        String firstName,

        @NotNull(message = "Last name is required")
        @Size(max = 50, message = "Last name cannot exceed 50 characters")
        String lastName,

        @NotNull(message = "Password is required")
        @Size(max = 255, message = "Password cannot exceed 255 characters")
        String password,

        @NotNull(message = "Email is required")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        String email
) {
}