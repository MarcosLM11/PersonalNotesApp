package com.marcos.personalNotesWebApplication.dtos.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record NoteResponseDto (
        UUID id,
        String title,
        String content,
        UserResponseDto author,
        String s3Url,
        String tags,
        boolean isPublic,
        Instant createdAt,
        Instant updatedAt,
        List<NoteVersionResponseDto> versions
) {}
