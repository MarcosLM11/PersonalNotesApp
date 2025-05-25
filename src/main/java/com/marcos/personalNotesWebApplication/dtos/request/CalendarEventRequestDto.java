package com.marcos.personalNotesWebApplication.dtos.request;

import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Temporal;

import java.util.Date;
import java.util.UUID;

public record CalendarEventRequestDto (
        @NotBlank(message = "Title is required")
        String title,
        String description,
        String location,
        @NotNull(message = "Start time is required")
        @Temporal(TemporalType.TIMESTAMP)
        Date startTime,
        @NotNull(message = "End time is required")
        @Temporal(TemporalType.TIMESTAMP)
        Date endTime,
        String googleCalendarId,
        UUID userId,
        boolean isGoogleSynced,
        boolean isAllDay
) {}
