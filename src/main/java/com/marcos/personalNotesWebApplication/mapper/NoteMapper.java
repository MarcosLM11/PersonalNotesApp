package com.marcos.personalNotesWebApplication.mapper;

import com.marcos.personalNotesWebApplication.dtos.request.NoteRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.NoteUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteVersionResponseDto;
import com.marcos.personalNotesWebApplication.entities.NoteEntity;
import com.marcos.personalNotesWebApplication.entities.NoteVersionEntity;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import com.marcos.personalNotesWebApplication.repositories.UserRepository;
import com.marcos.personalNotesWebApplication.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class NoteMapper {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public NoteMapper(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public NoteEntity toEntity(NoteRequestDto request) {
        if (request == null) {
            return null;
        }

        NoteEntity entity = new NoteEntity();
        entity.setTitle(request.title());
        entity.setContent(request.content());
        entity.setTags(request.tags());
        entity.setPublic(request.isPublic());
        entity.setS3Url(request.s3Url());
        
        // Set the author
        UserEntity author = userRepository.findById(UUID.fromString(request.authorId()))
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.authorId()));
        entity.setAuthor(author);
        
        return entity;
    }

    public NoteResponseDto toResponse(NoteEntity entity) {
        if (entity == null) {
            return null;
        }

        return new NoteResponseDto(
            entity.getId(),
            entity.getTitle(),
            entity.getContent(),
            entity.getAuthor() != null ? userMapper.toResponse(entity.getAuthor()) : null,
            entity.getS3Url(),
            entity.getTags(),
            entity.isPublic(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getVersions() != null ? toVersionResponseList(entity.getVersions()) : Collections.emptyList()
        );
    }

    public void updateEntityFromRequest(NoteUpdateDto request, NoteEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.title() != null) {
            entity.setTitle(request.title());
        }
        if (request.content() != null) {
            entity.setContent(request.content());
        }
        if (request.tags() != null) {
            entity.setTags(request.tags());
        }
        if (request.isPublic() != entity.isPublic()) {
            entity.setPublic(request.isPublic());
        }
        entity.setPublic(request.isPublic());
        if (request.s3Url() != null) {
            entity.setS3Url(request.s3Url());
        }
    }

    public NoteVersionResponseDto toVersionResponse(NoteVersionEntity versionEntity) {
        if (versionEntity == null) {
            return null;
        }

        return new NoteVersionResponseDto(
            versionEntity.getId(),
            versionEntity.getNote().getId(),
            versionEntity.getVersion(),
            versionEntity.getContent(),
            versionEntity.getCreatedAt(),
            versionEntity.getCreatedBy()
        );
    }

    private List<NoteVersionResponseDto> toVersionResponseList(List<NoteVersionEntity> versions) {
        if (versions == null) {
            return Collections.emptyList();
        }
        return versions.stream()
                .map(this::toVersionResponse)
                .collect(Collectors.toList());
    }
} 