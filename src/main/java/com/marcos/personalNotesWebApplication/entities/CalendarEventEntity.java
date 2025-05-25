package com.marcos.personalNotesWebApplication.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "calendar_events")
@Table(name = "calendar_events")
@EntityListeners(AuditingEntityListener.class)
public class CalendarEventEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "location")
    private String location;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(name = "google_calendar_id")
    private String googleCalendarId;

    @Column(name = "is_google_synced", nullable = false)
    private boolean isGoogleSynced = false;

    @Column(name = "is_all_day", nullable = false)
    private boolean isAllDay = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReminderEntity> reminders = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    public void addReminder(ReminderEntity reminder) {
        reminders.add(reminder);
        reminder.setEvent(this);
    }

    public void removeReminder(ReminderEntity reminder) {
        reminders.remove(reminder);
        reminder.setEvent(null);
    }

    @PrePersist
    protected void onCreate() {
        if (startTime != null && endTime != null && startTime.isAfter(endTime)) {
            throw new IllegalStateException("Start time must be before end time");
        }
    }
}
