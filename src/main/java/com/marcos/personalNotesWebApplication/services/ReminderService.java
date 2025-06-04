package com.marcos.personalNotesWebApplication.services;

import com.marcos.personalNotesWebApplication.dtos.request.ReminderRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.ReminderUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.response.ReminderResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import java.time.LocalDateTime;
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
     * @param sortBy the field to sort by
     * @return a page of reminder response data transfer objects
     */
    Page<ReminderResponseDto> getAllReminders(LocalDateTime startDate, LocalDateTime endDate, int page, int size, String sortBy);

    /**
     * Updates an existing reminder.
     *
     * @param id the UUID of the reminder to update
     * @param request the reminder request data transfer object containing updated reminder details
     * @return the updated reminder response data transfer object
     */
    ReminderResponseDto updateReminder(UUID id, @Valid ReminderUpdateDto request);

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
     * @param sortBy the field to sort by
     * @return a page of reminder response data transfer objects
     */
    Page<ReminderResponseDto> getUserReminders(UUID userId, int page, int size, String sortBy);

    /**
     * Retrieves all reminders for a specific user with slice pagination.
     *
     * @param userId the UUID of the user
     * @param page the page number to retrieve
     * @param size the number of reminders per page
     * @return a slice of reminder response data transfer objects
     */
    Slice<ReminderResponseDto> getUserRemindersSlice(UUID userId, int page, int size);

    /**
     * Retrieves all upcoming reminders with pagination.
     *
     * @param page the page number to retrieve
     * @param size the number of reminders per page
     * @param sortBy the field to sort by
     * @return a page of reminder response data transfer objects
     */
    Page<ReminderResponseDto> getUpcomingReminders(int page, int size, String sortBy);

    /**
     * Searches reminders by title or description with pagination.
     *
     * @param query the search query
     * @param page the page number to retrieve
     * @param size the number of reminders per page
     * @param sortBy the field to sort by
     * @return a page of reminder response data transfer objects
     */
    Page<ReminderResponseDto> searchReminders(String query, int page, int size, String sortBy);

    /**
     * Marks a reminder as completed.
     *
     * @param id the UUID of the reminder to mark as completed
     * @return the updated reminder response data transfer object
     */
    ReminderResponseDto markReminderAsCompleted(UUID id);

    /**
     * Checks if the given user is the owner of the reminder.
     *
     * @param reminderId the UUID of the reminder
     * @param username the username to check ownership for
     * @return true if the user owns the reminder, false otherwise
     */
    boolean isReminderOwner(UUID reminderId, String username);
}
