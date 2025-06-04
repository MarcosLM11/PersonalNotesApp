package com.marcos.personalNotesWebApplication.services;

import com.marcos.personalNotesWebApplication.dtos.request.NoteRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.NoteUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteVersionResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
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
     * @return a page of note response data transfer objects
     */
    Page<NoteResponseDto> getAllNotes(int page, int size, String sortBy);

    /**
     * Updates an existing note.
     *
     * @param id the UUID of the note to update
     * @param request the note request data transfer object containing updated note details
     * @return the updated note response data transfer object
     */
    NoteResponseDto updateNote(UUID id, @Valid NoteUpdateDto request);

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
     * @return a page of note response data transfer objects
     */
    Page<NoteResponseDto> getNotesByUser(UUID userId, int page, int size);

    /**
     * Retrieves notes for a specific user with slice-based pagination (more efficient).
     * Use this when you don't need total count information.
     *
     * @param userId the UUID of the user
     * @param page the page number to retrieve
     * @param size the number of notes per page
     * @return a slice of note response data transfer objects
     */
    Slice<NoteResponseDto> getNotesByUserSlice(UUID userId, int page, int size);

    /**
     * Search notes by content or title with pagination.
     *
     * @param searchTerm the search term
     * @param page the page number to retrieve
     * @param size the number of notes per page
     * @param sortBy the field to sort by
     * @return a page of matching note response data transfer objects
     */
    Page<NoteResponseDto> searchNotes(String searchTerm, int page, int size, String sortBy);

    /**
     * Retrieves all versions of a note by its ID.
     *
     * @param id the UUID of the note
     * @return a list of note version response data transfer objects
     */
    List<NoteVersionResponseDto> getNoteVersions(UUID id);

    /**
     * Checks if a user is the owner of a note.
     *
     * @param noteId the UUID of the note
     * @param username the username to check
     * @return true if the user owns the note, false otherwise
     */
    boolean isNoteOwner(UUID noteId, String username);
}
