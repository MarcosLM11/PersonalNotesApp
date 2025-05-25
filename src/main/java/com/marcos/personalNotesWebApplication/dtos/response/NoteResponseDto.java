package com.marcos.personalNotesWebApplication.dtos.response;

import java.time.LocalDateTime;
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
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<NoteVersionResponseDto> versions
) {}
