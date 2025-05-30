package com.marcos.personalNotesWebApplication.dtos.request;

import com.marcos.personalNotesWebApplication.enums.NotificationTypeEnum;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReminderRequestDto(
    @NotNull(message = "Event ID is required")
    UUID eventId,
    @NotNull(message = "Reminder time is required")
    Instant reminderTime,
    NotificationTypeEnum notificationType
) {}
