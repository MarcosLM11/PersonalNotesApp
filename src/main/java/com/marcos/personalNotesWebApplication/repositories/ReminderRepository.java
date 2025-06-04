package com.marcos.personalNotesWebApplication.repositories;

import com.marcos.personalNotesWebApplication.entities.ReminderEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
     * Finds all reminders that are scheduled between the specified start and end dates with pagination.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @param pageable  the pagination information
     * @return a page of reminders within the specified date range
     */
    Page<ReminderEntity> findAllByReminderTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

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
     * Finds all reminders created by a specific user with pagination.
     *
     * @param userId   the user ID
     * @param pageable the pagination information
     * @return a page of reminders created by the specified user
     */
    @Query("""
        SELECT r FROM reminders r 
        JOIN r.event e 
        WHERE e.user.id = :userId
    """)
    Page<ReminderEntity> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

    /**
     * Finds all reminders created by a specific user with slice pagination.
     *
     * @param userId   the user ID
     * @param pageable the pagination information
     * @return a slice of reminders created by the specified user
     */
    @Query("""
        SELECT r FROM reminders r 
        JOIN r.event e 
        WHERE e.user.id = :userId
    """)
    Slice<ReminderEntity> findAllByUserIdSlice(@Param("userId") UUID userId, Pageable pageable);

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

    /**
     * Finds all reminders that are scheduled after the specified time with pagination.
     *
     * @param now      the current time to compare against
     * @param pageable the pagination information
     * @return a page of reminders that are scheduled after the specified time
     */
    @Query("""
        SELECT r FROM reminders r 
        WHERE r.reminderTime > :now 
        AND r.sent = false 
        ORDER BY r.reminderTime ASC
    """)
    Page<ReminderEntity> findAllByReminderTimeAfter(@Param("now") LocalDateTime now, Pageable pageable);

    /**
     * Searches reminders by title or description of the associated event.
     *
     * @param query    the search query
     * @param pageable the pagination information
     * @return a page of reminders matching the search criteria
     */
    @Query("""
        SELECT r FROM reminders r 
        JOIN r.event e 
        WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :query, '%')) 
        OR LOWER(e.description) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    Page<ReminderEntity> searchByEventTitleOrDescription(@Param("query") String query, Pageable pageable);

    /**
     * Finds a reminder by ID and checks if it belongs to the specified user.
     *
     * @param reminderId the reminder ID
     * @param username   the username to check ownership for
     * @return true if the reminder exists and belongs to the user, false otherwise
     */
    @Query("""
        SELECT COUNT(r) > 0 FROM reminders r 
        JOIN r.event e 
        JOIN e.user u 
        WHERE r.id = :reminderId AND u.username = :username
    """)
    boolean existsByIdAndUsername(@Param("reminderId") UUID reminderId, @Param("username") String username);
}
