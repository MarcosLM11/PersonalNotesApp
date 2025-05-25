package com.marcos.personalNotesWebApplication.entities;

import com.marcos.personalNotesWebApplication.enums.NotificationTypeEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "reminders")
@Table(name = "reminders")
@EntityListeners(AuditingEntityListener.class)
@ToString(exclude = "event")
@EqualsAndHashCode(of = "id")
public class ReminderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private CalendarEventEntity event;

    @NotNull
    @Column(name = "reminder_time", nullable = false)
    private Date reminderTime;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum notificationType;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "sent", nullable = false)
    private boolean sent = false;

    @PrePersist
    protected void onCreate() {
        if (reminderTime != null && event != null && 
            reminderTime.after(event.getStartTime())) {
            throw new IllegalStateException("Reminder time must be before event start time");
        }
    }

    // Helper method to get the user through the event
    public UserEntity getUser() {
        return event != null ? event.getUser() : null;
    }
}
