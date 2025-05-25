package com.marcos.personalNotesWebApplication.mapper;

import com.marcos.personalNotesWebApplication.dtos.request.UserRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.UserUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.response.UserResponseDto;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(UserRequestDto request) {
        if (request == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setUsername(request.username());
        entity.setEmail(request.email());
        entity.setPassword(request.password());
        entity.setFullName(request.fullName());
        entity.setPhoneNumber(request.phoneNumber());
        entity.setProfileImageUrl(request.profilePictureUrl());
        return entity;
    }

    public UserResponseDto toResponse(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return new UserResponseDto(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getFullName(),
            entity.getPhoneNumber(),
            entity.getProfileImageUrl(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public void updateEntityFromRequest(UserUpdateDto request, UserEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.username() != null) {
            entity.setUsername(request.username());
        }
        if (request.email() != null) {
            entity.setEmail(request.email());
        }
        if (request.fullName() != null) {
            entity.setFullName(request.fullName());
        }
        if (request.phoneNumber() != null) {
            entity.setPhoneNumber(request.phoneNumber());
        }
        if (request.profilePictureUrl() != null) {
            entity.setProfileImageUrl(request.profilePictureUrl());
        }
    }
} 