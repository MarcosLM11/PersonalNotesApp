package com.marcos.personalNotesWebApplication.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,
        @Email(message = "Invalid email format")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,
        @Size(max = 100, message = "Full name must not exceed 100 characters")
        String fullName,
        @Size(max = 15, message = "Phone number must not exceed 15 characters")
        String phoneNumber,
        @Size(max = 255, message = "Profile picture URL must not exceed 255 characters")
        String profilePictureUrl
) {
}
