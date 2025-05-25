package com.marcos.personalNotesWebApplication.dtos.response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record NoteVersionResponseDto(
    UUID id,
    UUID noteId,
    int version,
    String content,
    LocalDateTime createdAt,
    String createdBy
) {}
