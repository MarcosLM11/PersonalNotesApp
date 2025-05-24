package com.marcos.personalNotesWebApplication.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDto(
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 120, message = "Password must be between 8 and 120 characters")
    String password,
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    String email,
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    String fullName,
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    String phoneNumber,
    String profilePictureUrl
) {}

