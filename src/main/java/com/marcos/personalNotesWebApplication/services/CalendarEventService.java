package com.marcos.personalNotesWebApplication.services;

import com.marcos.personalNotesWebApplication.dtos.request.CalendarEventRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.CalendarEventUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.request.ReminderRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.CalendarEventResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.ReminderResponseDto;
import jakarta.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface CalendarEventService {

    /**
     * Creates a new calendar event.
     *
     * @param request the calendar event request data transfer object containing event details
     * @return the created calendar event response data transfer object
     */
    CalendarEventResponseDto createEvent(@Valid CalendarEventRequestDto request);

    /**
     * Retrieves a calendar event by its ID.
     *
     * @param id the UUID of the calendar event
     * @return the calendar event response data transfer object
     */
    CalendarEventResponseDto getEventById(UUID id);

    /**
     * Retrieves all calendar events within a specified date range with pagination.
     *
     * @param startDate the start date of the range
     * @param endDate the end date of the range
     * @param page the page number to retrieve
     * @param size the number of events per page
     * @return a list of calendar event response data transfer objects
     */
    List<CalendarEventResponseDto> getAllEvents(Instant startDate, Instant endDate, int page, int size);

    /**
     * Updates an existing calendar event.
     *
     * @param id the UUID of the calendar event to update
     * @param request the calendar event request data transfer object containing updated event details
     * @return the updated calendar event response data transfer object
     */
    CalendarEventResponseDto updateEvent(UUID id, @Valid CalendarEventUpdateDto request);

    /**
     * Deletes a calendar event by its ID.
     *
     * @param id the UUID of the calendar event to delete
     */
    void deleteEvent(UUID id);

    /**
     * Adds a reminder to a calendar event.
     * @param eventId the UUID of the calendar event to which the reminder will be added
     * @param request the reminder request data transfer object containing reminder details
     * @return the created reminder response data transfer object
     */
    ReminderResponseDto addReminder(UUID eventId, @Valid ReminderRequestDto request);

    /**
     * Retrieves a reminder by its ID for a specific calendar event.
     *
     * @param eventId the UUID of the calendar event
     * @param reminderId the UUID of the reminder
     * @return the reminder response data transfer object
     */
    ReminderResponseDto getReminderById(UUID eventId, UUID reminderId);

    /**
     * Retrieves all reminders for a specific calendar event.
     *
     * @param eventId the UUID of the calendar event
     * @return a list of reminder response data transfer objects
     */
    List<ReminderResponseDto> getEventReminders(UUID eventId);

    /**
     * Deletes a reminder from a calendar event.
     * @param eventId the UUID of the calendar event from which the reminder will be deleted
     * @param reminderId the UUID of the reminder to delete
     */
    void deleteReminder(UUID eventId, UUID reminderId);
}
