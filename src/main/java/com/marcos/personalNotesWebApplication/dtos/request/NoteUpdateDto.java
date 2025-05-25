package com.marcos.personalNotesWebApplication.dtos.request;

public record NoteUpdateDto(
        String title,
        String content,
        String tags,
        boolean isPublic,
        String s3Url
) {
}
