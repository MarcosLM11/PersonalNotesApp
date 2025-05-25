package com.marcos.personalNotesWebApplication.dtos.request;

import jakarta.persistence.TemporalType;
import org.springframework.data.jpa.repository.Temporal;

import java.util.Date;

public record CalendarEventUpdateDto(
        String title,
        String description,
        String location,
        Boolean isAllDay,
        String googleCalendarId,
        Boolean isGoogleSynced,
        @Temporal(TemporalType.TIMESTAMP)
        Date startTime,
        @Temporal(TemporalType.TIMESTAMP)
        Date endTime
) {
}
