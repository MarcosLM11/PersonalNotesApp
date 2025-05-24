package com.marcos.personalNotesWebApplication.repositories;

import com.marcos.personalNotesWebApplication.entities.ReminderEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.Instant;
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
    List<ReminderEntity> findAllByReminderTimeBetween(Instant startDate, Instant endDate);

    /**
     * Finds all reminders created by a specific user.
     *
     * @param username the username of the user who created the reminders
     * @return a list of reminders created by the specified user
     */
    List<ReminderEntity> findAllByCreatedBy(@NotBlank @Size(max = 50) String username);

    /**
     * Finds all reminders that are scheduled after the specified time.
     *
     * @param now the current time to compare against
     * @return a list of reminders that are scheduled after the specified time
     */
    List<ReminderEntity> findAllByReminderTimeAfter(Instant now);
}
