package com.marcos.personalNotesWebApplication.controllers;

import com.marcos.personalNotesWebApplication.dtos.request.NoteRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteVersionResponseDto;
import com.marcos.personalNotesWebApplication.services.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NoteResponseDto> createNote(
            @Valid @RequestBody NoteRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noteService.createNote(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteResponseDto> getNoteById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponseDto>> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {
        return ResponseEntity.ok(noteService.getAllNotes(page, size, sortBy));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@noteService.isNoteOwner(#id, principal.username)")
    public ResponseEntity<NoteResponseDto> updateNote(
            @PathVariable UUID id,
            @Valid @RequestBody NoteRequestDto request) {
        return ResponseEntity.ok(noteService.updateNote(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@noteService.isNoteOwner(#id, principal.username)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NoteResponseDto>> getNotesByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(noteService.getNotesByUser(userId, page, size));
    }

    @GetMapping("/{id}/versions")
    public ResponseEntity<List<NoteVersionResponseDto>> getNoteVersions(
            @PathVariable UUID id) {
        return ResponseEntity.ok(noteService.getNoteVersions(id));
    }
} 