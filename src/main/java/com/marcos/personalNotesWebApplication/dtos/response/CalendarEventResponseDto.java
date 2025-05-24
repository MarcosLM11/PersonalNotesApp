package com.marcos.personalNotesWebApplication.dtos.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CalendarEventResponseDto (
        UUID id,
        String eventId,
        String title,
        String description,
        String location,
        Instant startTime,
        Instant endTime,
        String googleCalendarId,
        boolean isGoogleSynced,
        boolean isAllDay,
        List<ReminderResponseDto> reminders,
        Instant createdAt,
        String createdBy,
        Instant updatedAt
) {}
