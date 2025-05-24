package com.marcos.personalNotesWebApplication.services.impl;

import com.marcos.personalNotesWebApplication.dtos.request.NoteRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteVersionResponseDto;
import com.marcos.personalNotesWebApplication.entities.NoteEntity;
import com.marcos.personalNotesWebApplication.entities.NoteVersionEntity;
import com.marcos.personalNotesWebApplication.exceptions.ResourceNotFoundException;
import com.marcos.personalNotesWebApplication.repositories.NoteRepository;
import com.marcos.personalNotesWebApplication.repositories.NoteVersionRepository;
import com.marcos.personalNotesWebApplication.services.NoteService;
import com.marcos.personalNotesWebApplication.utils.IsNullOrEmptyUtil;
import com.marcos.personalNotesWebApplication.mapper.NoteMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final IsNullOrEmptyUtil isNullOrEmptyUtil;
    private final NoteVersionRepository noteVersionRepository;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper, IsNullOrEmptyUtil isNullOrEmptyUtil,
                           NoteVersionRepository noteVersionRepository) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.isNullOrEmptyUtil = isNullOrEmptyUtil;
        this.noteVersionRepository = noteVersionRepository;
    }

    @Override
    public NoteResponseDto createNote(NoteRequestDto request) {
        if (isNullOrEmptyUtil.isNullOrEmpty(request)) {
            throw new IllegalArgumentException("Note request cannot be null");
        }

        var noteEntity = noteMapper.toEntity(request);
        noteRepository.save(noteEntity);
        return noteMapper.toResponse(noteEntity);
    }

    @Override
    public NoteResponseDto getNoteById(UUID id) {
        return noteRepository.findById(id)
                .map(noteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
    }

    @Override
    public List<NoteResponseDto> getAllNotes(int page, int size, String sortBy) {
        return noteRepository.findAll() // Implement pagination and sorting as needed
                .stream()
                .map(noteMapper::toResponse)
                .toList();
    }

    @Override
    public NoteResponseDto updateNote(UUID id, NoteRequestDto request) {
        if (isNullOrEmptyUtil.isNullOrEmpty(request)) {
            throw new IllegalArgumentException("Note request cannot be null");
        }

        return noteRepository.findById(id)
                .map(existingNote -> {
                    noteMapper.updateEntityFromRequest(request, existingNote);
                    noteRepository.save(existingNote);
                    return noteMapper.toResponse(existingNote);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
    }

    @Override
    public void deleteNote(UUID id) {
        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
        noteRepository.delete(noteEntity);
    }

    @Override
    public List<NoteResponseDto> getNotesByUser(UUID userId, int page, int size) {
        return noteRepository.findByAuthorId(userId) // Implement pagination and sorting as needed
                .stream()
                .map(noteMapper::toResponse)
                .toList();
    }

    @Override
    public List<NoteVersionResponseDto> getNoteVersions(UUID id) {
        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
        List<NoteVersionEntity> latestVersion = noteVersionRepository.findByNote(noteEntity);

        if (isNullOrEmptyUtil.isNullOrEmpty(latestVersion)) {
            throw new ResourceNotFoundException("No versions found for note with id: " + id);
        }
        return latestVersion.stream().map(noteMapper::toVersionResponse).toList();
    }
}
