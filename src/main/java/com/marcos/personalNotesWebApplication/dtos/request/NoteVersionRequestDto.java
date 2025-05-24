package com.marcos.personalNotesWebApplication.dtos.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record NoteVersionRequestDto(
        @NotNull(message = "Note ID is required")
        UUID noteId,
        @NotNull(message = "Content is required")
        String content
) {}
