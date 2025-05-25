package com.marcos.personalNotesWebApplication.dtos.request;

import com.marcos.personalNotesWebApplication.enums.NotificationTypeEnum;
import java.time.LocalDateTime;

public record ReminderUpdateDto(
    LocalDateTime reminderTime,
    NotificationTypeEnum notificationType
) {}
