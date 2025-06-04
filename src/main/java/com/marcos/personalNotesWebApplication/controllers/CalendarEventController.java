package com.marcos.personalNotesWebApplication.controllers;

import com.marcos.personalNotesWebApplication.dtos.request.CalendarEventRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.CalendarEventUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.request.ReminderRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.CalendarEventResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.PageResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.ReminderResponseDto;
import com.marcos.personalNotesWebApplication.services.CalendarEventService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/calendar-events")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:5173"})
public class CalendarEventController {

    private final CalendarEventService calendarEventService;

    public CalendarEventController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CalendarEventResponseDto> createEvent(
            @Valid @RequestBody CalendarEventRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(calendarEventService.createEvent(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarEventResponseDto> getEventById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(calendarEventService.getEventById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponseDto<CalendarEventResponseDto>> getAllEvents(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime") String sortBy) {
        var eventPage = calendarEventService.getAllEvents(startDate, endDate, page, size, sortBy);
        return ResponseEntity.ok(PageResponseDto.from(eventPage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendarEventResponseDto> updateEvent(
            @PathVariable UUID id,
            @Valid @RequestBody CalendarEventUpdateDto request) {
        return ResponseEntity.ok(calendarEventService.updateEvent(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@calendarEventService.isEventOwner(#id, principal.username)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id) {
        calendarEventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{eventId}/reminders")
    @PreAuthorize("@calendarEventService.isEventOwner(#eventId, principal.username)")
    public ResponseEntity<ReminderResponseDto> addReminder(
            @PathVariable UUID eventId,
            @Valid @RequestBody ReminderRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(calendarEventService.addReminder(eventId, request));
    }

    @GetMapping("/{eventId}/reminders")
    public ResponseEntity<List<ReminderResponseDto>> getEventReminders(
            @PathVariable UUID eventId) {
        return ResponseEntity.ok(calendarEventService.getEventReminders(eventId));
    }

    @DeleteMapping("/{eventId}/reminders/{reminderId}")
    @PreAuthorize("@calendarEventService.isEventOwner(#eventId, principal.username)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteReminder(
            @PathVariable UUID eventId,
            @PathVariable UUID reminderId) {
        calendarEventService.deleteReminder(eventId, reminderId);
        return ResponseEntity.noContent().build();
    }
} 