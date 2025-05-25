package com.marcos.personalNotesWebApplication.dtos.request;

import java.time.LocalDateTime;

public record CalendarEventUpdateDto(
        String title,
        String description,
        String location,
        Boolean isAllDay,
        String googleCalendarId,
        Boolean isGoogleSynced,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
