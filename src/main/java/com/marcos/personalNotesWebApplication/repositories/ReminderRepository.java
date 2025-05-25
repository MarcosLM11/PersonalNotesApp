package com.marcos.personalNotesWebApplication.repositories;

import com.marcos.personalNotesWebApplication.entities.ReminderEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, UUID> {

    /**
     * Finds all reminders that are scheduled between the specified start and end dates.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @return a list of reminders within the specified date range
     */
    List<ReminderEntity> findAllByReminderTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Finds all reminders created by a specific user.
     *
     * @param username the username of the user who created the reminders
     * @return a list of reminders created by the specified user
     */
    @Query(value ="""
        select r.id, r.reminder_time, r.notification_type, r.event_id, r.created_at, r.created_by, r.sent
        from reminders r
        left join calendar_events ce on ce.id = r.event_id
        left join users u on u.id = ce.user_id
        where u.id = :userid
    """, nativeQuery = true)
    List<ReminderEntity> findAllByCreatedBy(@NotBlank @Size(max = 50) @Param("userid") UUID username);

    /**
     * Finds all reminders that are scheduled after the specified time.
     *
     * @param now the current time to compare against
     * @return a list of reminders that are scheduled after the specified time
     */
    @Query("""
        SELECT r FROM reminders r 
        WHERE r.reminderTime > :now 
        AND r.sent = false 
        ORDER BY r.reminderTime ASC
    """)
    List<ReminderEntity> findAllByReminderTimeAfter(@Param("now") LocalDateTime now);
}
