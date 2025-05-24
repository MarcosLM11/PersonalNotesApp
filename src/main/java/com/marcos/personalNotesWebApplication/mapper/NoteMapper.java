package com.marcos.personalNotesWebApplication.mapper;

import com.marcos.personalNotesWebApplication.dtos.request.NoteRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.NoteVersionResponseDto;
import com.marcos.personalNotesWebApplication.entities.NoteEntity;
import com.marcos.personalNotesWebApplication.entities.NoteVersionEntity;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NoteMapper {

    private final UserMapper userMapper;

    public NoteMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
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

    public void updateEntityFromRequest(NoteRequestDto request, NoteEntity entity) {
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
        if (request.s3Url() != null) {
            entity.setS3Url(request.s3Url());
        }
        entity.setPublic(request.isPublic());
    }

    public NoteVersionResponseDto toVersionResponse(NoteVersionEntity entity) {
        if (entity == null) {
            return null;
        }

        return new NoteVersionResponseDto(
            entity.getId(),
            entity.getVersion(),
            entity.getContent(),
            entity.getCreatedAt(),
            entity.getCreatedBy()
        );
    }

    public List<NoteVersionResponseDto> toVersionResponseList(List<NoteVersionEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
            .map(this::toVersionResponse)
            .collect(Collectors.toList());
    }
} 