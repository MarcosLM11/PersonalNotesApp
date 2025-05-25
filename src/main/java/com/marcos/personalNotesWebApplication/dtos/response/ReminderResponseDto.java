package com.marcos.personalNotesWebApplication.dtos.response;

import com.marcos.personalNotesWebApplication.enums.NotificationTypeEnum;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public record ReminderResponseDto(
    UUID id,
    Date reminderTime,
    NotificationTypeEnum notificationType,
    boolean sent,
    Instant createdAt,
    String createdBy
) {}
