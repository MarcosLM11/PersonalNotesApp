package com.marcos.personalNotesWebApplication.mapper;

import com.marcos.personalNotesWebApplication.dtos.request.CalendarEventRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.CalendarEventResponseDto;
import com.marcos.personalNotesWebApplication.entities.CalendarEventEntity;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
public class CalendarEventMapper {

    private final ReminderMapper reminderMapper;

    public CalendarEventMapper(ReminderMapper reminderMapper) {
        this.reminderMapper = reminderMapper;
    }

    public CalendarEventEntity toEntity(CalendarEventRequestDto request, UserEntity user) {
        if (request == null) {
            return null;
        }

        CalendarEventEntity entity = new CalendarEventEntity();
        entity.setTitle(request.title());
        entity.setDescription(request.description());
        entity.setLocation(request.location());
        entity.setStartTime(request.startTime());
        entity.setEndTime(request.endTime());
        entity.setGoogleCalendarId(request.googleCalendarId());
        entity.setGoogleSynced(request.isGoogleSynced());
        entity.setAllDay(request.isAllDay());
        entity.setUser(user);
        return entity;
    }

    public CalendarEventResponseDto toResponse(CalendarEventEntity entity) {
        if (entity == null) {
            return null;
        }

        return new CalendarEventResponseDto(
            entity.getId(),
            entity.getId().toString(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getLocation(),
            entity.getStartTime(),
            entity.getEndTime(),
            entity.getGoogleCalendarId(),
            entity.isGoogleSynced(),
            entity.isAllDay(),
            entity.getReminders() != null ? reminderMapper.toResponseList(entity.getReminders()) : Collections.emptyList(),
            entity.getCreatedAt(),
            entity.getCreatedBy(),
            entity.getUpdatedAt()
        );
    }

    public void updateEntityFromRequest(CalendarEventRequestDto request, CalendarEventEntity entity) {
        if (request == null || entity == null) {
            return;
        }

        if (request.title() != null) {
            entity.setTitle(request.title());
        }
        if (request.description() != null) {
            entity.setDescription(request.description());
        }
        if (request.location() != null) {
            entity.setLocation(request.location());
        }
        if (request.startTime() != null) {
            entity.setStartTime(request.startTime());
        }
        if (request.endTime() != null) {
            entity.setEndTime(request.endTime());
        }
        if (request.googleCalendarId() != null) {
            entity.setGoogleCalendarId(request.googleCalendarId());
        }
        entity.setGoogleSynced(request.isGoogleSynced());
        entity.setAllDay(request.isAllDay());
    }
} 