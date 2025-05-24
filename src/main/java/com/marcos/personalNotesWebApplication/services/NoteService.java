package com.marcos.personalNotesWebApplication.services;

import com.marcos.personalNotesWebApplication.dtos.request.NoteRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteVersionResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface NoteService {

    /**
     * Creates a new note.
     *
     * @param request the note request data transfer object containing note details
     * @return the created note response data transfer object
     */
    NoteResponseDto createNote(@Valid NoteRequestDto request);

    /**
     * Retrieves a note by its ID.
     *
     * @param id the UUID of the note
     * @return the note response data transfer object
     */
    NoteResponseDto getNoteById(UUID id);

    /**
     * Retrieves all notes with pagination and sorting.
     *
     * @param page the page number to retrieve
     * @param size the number of notes per page
     * @param sortBy the field to sort by
     * @return a list of note response data transfer objects
     */
    List<NoteResponseDto> getAllNotes(int page, int size, String sortBy);

    /**
     * Updates an existing note.
     *
     * @param id the UUID of the note to update
     * @param request the note request data transfer object containing updated note details
     * @return the updated note response data transfer object
     */
    NoteResponseDto updateNote(UUID id, @Valid NoteRequestDto request);

    /**
     * Deletes a note by its ID.
     *
     * @param id the UUID of the note to delete
     */
    void deleteNote(UUID id);

    /**
     * Retrieves all notes for a specific user with pagination.
     *
     * @param userId the UUID of the user
     * @param page the page number to retrieve
     * @param size the number of notes per page
     * @return a list of note response data transfer objects
     */
    List<NoteResponseDto> getNotesByUser(UUID userId, int page, int size);

    /**
     * Retrieves all versions of a note by its ID.
     *
     * @param id the UUID of the note
     * @return a list of note version response data transfer objects
     */
    List<NoteVersionResponseDto> getNoteVersions(UUID id);
}
