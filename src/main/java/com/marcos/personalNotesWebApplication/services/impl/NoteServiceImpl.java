package com.marcos.personalNotesWebApplication.services.impl;

import com.marcos.personalNotesWebApplication.dtos.request.NoteRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.NoteUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteVersionResponseDto;
import com.marcos.personalNotesWebApplication.entities.NoteEntity;
import com.marcos.personalNotesWebApplication.entities.NoteVersionEntity;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import com.marcos.personalNotesWebApplication.exceptions.ResourceNotFoundException;
import com.marcos.personalNotesWebApplication.repositories.NoteRepository;
import com.marcos.personalNotesWebApplication.repositories.NoteVersionRepository;
import com.marcos.personalNotesWebApplication.repositories.UserRepository;
import com.marcos.personalNotesWebApplication.services.NoteService;
import com.marcos.personalNotesWebApplication.utils.IsNullOrEmptyUtil;
import com.marcos.personalNotesWebApplication.mapper.NoteMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;
    private final IsNullOrEmptyUtil isNullOrEmptyUtil;
    private final NoteVersionRepository noteVersionRepository;
    private final UserRepository userRepository;

    // Configuración por defecto para paginación
    private static final int MAX_PAGE_SIZE = 100;
    private static final String DEFAULT_SORT_FIELD = "createdAt";
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper, IsNullOrEmptyUtil isNullOrEmptyUtil,
                           NoteVersionRepository noteVersionRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
        this.isNullOrEmptyUtil = isNullOrEmptyUtil;
        this.noteVersionRepository = noteVersionRepository;
        this.userRepository = userRepository;
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
    @Transactional(readOnly = true)
    public NoteResponseDto getNoteById(UUID id) {
        return noteRepository.findById(id)
                .map(noteMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteResponseDto> getAllNotes(int page, int size, String sortBy) {
        // Validar y ajustar parámetros de paginación
        Pageable pageable = createPageable(page, size, sortBy);
        
        Page<NoteEntity> notePage = noteRepository.findAll(pageable);
        
        // Mapear entidades a DTOs manteniendo la información de paginación
        return notePage.map(noteMapper::toResponse);
    }

    @Override
    public NoteResponseDto updateNote(UUID id, NoteUpdateDto request) {
        if (isNullOrEmptyUtil.isNullOrEmpty(request)) {
            throw new IllegalArgumentException("Note request cannot be null");
        }

        return noteRepository.findById(id)
                .map(existingNote -> {
                    // Crear versión antes de actualizar
                    createNoteVersion(existingNote);
                    
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
    @Transactional(readOnly = true)
    public Page<NoteResponseDto> getNotesByUser(UUID userId, int page, int size) {
        // Validar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        Pageable pageable = createPageable(page, size, DEFAULT_SORT_FIELD);
        Page<NoteEntity> notePage = noteRepository.findByAuthorId(userId, pageable);
        
        return notePage.map(noteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<NoteResponseDto> getNotesByUserSlice(UUID userId, int page, int size) {
        // Validar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        Pageable pageable = createPageable(page, size, DEFAULT_SORT_FIELD);
        Slice<NoteEntity> noteSlice = noteRepository.findByAuthorIdOrderByCreatedAtDesc(userId, pageable);
        
        return noteSlice.map(noteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NoteResponseDto> searchNotes(String searchTerm, int page, int size, String sortBy) {
        if (!StringUtils.hasText(searchTerm)) {
            throw new IllegalArgumentException("Search term cannot be empty");
        }

        Pageable pageable = createPageable(page, size, sortBy);
        Page<NoteEntity> notePage = noteRepository.searchNotes(searchTerm.trim(), pageable);
        
        return notePage.map(noteMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NoteVersionResponseDto> getNoteVersions(UUID id) {
        NoteEntity noteEntity = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found with id: " + id));
        List<NoteVersionEntity> versions = noteVersionRepository.findByNote(noteEntity);
        return versions.stream().map(noteMapper::toVersionResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNoteOwner(UUID noteId, String username) {
        return noteRepository.findById(noteId)
                .map(note -> note.getAuthor().getUsername().equals(username))
                .orElse(false);
    }

    /**
     * Crea un objeto Pageable validando y limitando los parámetros.
     */
    private Pageable createPageable(int page, int size, String sortBy) {
        // Validar página
        int validPage = Math.max(0, page);
        
        // Limitar y validar tamaño de página
        int validSize = Math.min(Math.max(1, size), MAX_PAGE_SIZE);
        
        // Validar campo de ordenación
        String validSortBy = StringUtils.hasText(sortBy) ? sortBy : DEFAULT_SORT_FIELD;
        
        // Crear el sort con dirección por defecto
        Sort sort = Sort.by(DEFAULT_SORT_DIRECTION, validSortBy);
        
        return PageRequest.of(validPage, validSize, sort);
    }

    /**
     * Crea una nueva versión de la nota antes de actualizar.
     */
    private void createNoteVersion(NoteEntity note) {
        NoteVersionEntity version = new NoteVersionEntity();
        version.setContent(note.getContent());
        version.setNote(note);
        
        // El número de versión se asigna automáticamente en addVersion
        note.addVersion(version);
    }
}
