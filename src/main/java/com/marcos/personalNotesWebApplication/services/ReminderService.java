package com.marcos.personalNotesWebApplication.services;

import com.marcos.personalNotesWebApplication.dtos.request.ReminderRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.ReminderResponseDto;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReminderService {

    /**
     * Creates a new reminder.
     *
     * @param request the reminder request data transfer object containing reminder details
     * @return the created reminder response data transfer object
     */
    ReminderResponseDto createReminder(@Valid ReminderRequestDto request);

    /**
     * Retrieves a reminder by its ID.
     *
     * @param id the UUID of the reminder
     * @return the reminder response data transfer object
     */
    ReminderResponseDto getReminderById(UUID id);

    /**
     * Retrieves all reminders within a specified date range with pagination.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @param page the page number to retrieve
     * @param size the number of reminders per page
     * @return a list of reminder response data transfer objects
     */
    List<ReminderResponseDto> getAllReminders(Instant startDate, Instant endDate, int page, int size);

    /**
     * Updates an existing reminder.
     *
     * @param id the UUID of the reminder to update
     * @param request the reminder request data transfer object containing updated reminder details
     * @return the updated reminder response data transfer object
     */
    ReminderResponseDto updateReminder(UUID id, @Valid ReminderRequestDto request);

    /**
     * Deletes a reminder by its ID.
     *
     * @param id the UUID of the reminder to delete
     */
    void deleteReminder(UUID id);

    /**
     * Retrieves all reminders for a specific user with pagination.
     *
     * @param userId the UUID of the user
     * @param page the page number to retrieve
     * @param size the number of reminders per page
     * @return a list of reminder response data transfer objects
     */
    List<ReminderResponseDto> getUserReminders(UUID userId, int page, int size);

    /**
     * Retrieves all upcoming reminders with pagination.
     *
     * @param page the page number to retrieve
     * @param size the number of reminders per page
     * @return a list of reminder response data transfer objects
     */
    List<ReminderResponseDto> getUpcomingReminders(int page, int size);

    /**
     * Marks a reminder as completed.
     *
     * @param id the UUID of the reminder to mark as completed
     * @return the updated reminder response data transfer object
     */
    ReminderResponseDto markReminderAsCompleted(UUID id);
}
