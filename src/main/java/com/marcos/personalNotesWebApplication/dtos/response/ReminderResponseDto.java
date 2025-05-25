package com.marcos.personalNotesWebApplication.dtos.response;

import com.marcos.personalNotesWebApplication.enums.NotificationTypeEnum;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReminderResponseDto(
    UUID id,
    LocalDateTime reminderTime,
    NotificationTypeEnum notificationType,
    boolean sent,
    LocalDateTime createdAt,
    String createdBy
) {}
