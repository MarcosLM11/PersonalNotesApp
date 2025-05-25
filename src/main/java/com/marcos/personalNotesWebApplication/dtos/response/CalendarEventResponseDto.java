package com.marcos.personalNotesWebApplication.dtos.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CalendarEventResponseDto (
        UUID id,
        String title,
        String description,
        String location,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String googleCalendarId,
        boolean isGoogleSynced,
        boolean isAllDay,
        List<ReminderResponseDto> reminders,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt
) {}
