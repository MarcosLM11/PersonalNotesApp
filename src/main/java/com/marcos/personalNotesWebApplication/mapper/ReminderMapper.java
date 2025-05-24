package com.marcos.personalNotesWebApplication.mapper;

import com.marcos.personalNotesWebApplication.dtos.request.ReminderRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.ReminderResponseDto;
import com.marcos.personalNotesWebApplication.entities.ReminderEntity;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReminderMapper {

    public ReminderEntity toEntity(ReminderRequestDto request) {
        if (request == null) {
            return null;
        }

        ReminderEntity entity = new ReminderEntity();
        entity.setReminderTime(request.reminderTime());
        entity.setNotificationType(request.notificationType());
        return entity;
    }

    public ReminderResponseDto toResponse(ReminderEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ReminderResponseDto(
            entity.getId(),
            entity.getReminderTime(),
            entity.getNotificationType(),
            entity.isSent(),
            entity.getCreatedAt(),
            entity.getCreatedBy()
        );
    }

    public List<ReminderResponseDto> toResponseList(List<ReminderEntity> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }

        return entities.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(ReminderRequestDto request, ReminderEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.reminderTime() != null) {
            entity.setReminderTime(request.reminderTime());
        }
        if (request.notificationType() != null) {
            entity.setNotificationType(request.notificationType());
        }
    }
} 