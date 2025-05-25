package com.marcos.personalNotesWebApplication.dtos.response;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public record CalendarEventResponseDto (
        UUID id,
        String title,
        String description,
        String location,
        Date startTime,
        Date endTime,
        String googleCalendarId,
        boolean isGoogleSynced,
        boolean isAllDay,
        List<ReminderResponseDto> reminders,
        Instant createdAt,
        String createdBy,
        Instant updatedAt
) {}
