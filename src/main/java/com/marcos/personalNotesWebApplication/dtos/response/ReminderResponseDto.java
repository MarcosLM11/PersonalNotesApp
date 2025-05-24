package com.marcos.personalNotesWebApplication.dtos.response;

import com.marcos.personalNotesWebApplication.enums.NotificationTypeEnum;

import java.time.Instant;
import java.util.UUID;

public record ReminderResponseDto (
        UUID id,
        Instant reminderTime,
        NotificationTypeEnum notificationType,
        boolean sent,
        Instant createdAt,
        String createdBy
) {}
