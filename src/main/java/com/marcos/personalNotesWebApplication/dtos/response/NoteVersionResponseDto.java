package com.marcos.personalNotesWebApplication.dtos.response;

import java.time.Instant;
import java.util.UUID;

public record NoteVersionResponseDto(
    UUID id,
    UUID noteId,
    int version,
    String content,
    Instant createdAt,
    String createdBy
) {}
