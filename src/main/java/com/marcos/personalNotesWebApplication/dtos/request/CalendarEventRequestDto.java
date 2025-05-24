package com.marcos.personalNotesWebApplication.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record CalendarEventRequestDto (
        @NotBlank(message = "Title is required")
        String title,
        String description,
        String location,
        @NotNull(message = "Start time is required")
        Instant startTime,
        @NotNull(message = "End time is required")
        Instant endTime,
        String googleCalendarId,
        boolean isGoogleSynced,
        boolean isAllDay
) {}
