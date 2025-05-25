package com.marcos.personalNotesWebApplication.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NoteRequestDto (
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,
        @NotBlank(message = "Author ID is required")
        String authorId,
        String content,
        @Size(max = 500, message = "Tags must not exceed 500 characters")
        String tags,
        boolean isPublic,
        String s3Url
) {}
