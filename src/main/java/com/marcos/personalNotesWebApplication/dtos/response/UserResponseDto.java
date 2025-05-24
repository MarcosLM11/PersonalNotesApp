package com.marcos.personalNotesWebApplication.dtos.response;
import java.time.Instant;
import java.util.UUID;

public record UserResponseDto (
        UUID id,
        String username,
        String email,
        String fullName,
        String phoneNumber,
        String profileImageUrl,
        Instant createdAt,
        Instant updatedAt
) {}
