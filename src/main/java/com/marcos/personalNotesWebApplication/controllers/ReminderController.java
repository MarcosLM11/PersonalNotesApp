package com.marcos.personalNotesWebApplication.controllers;

import com.marcos.personalNotesWebApplication.dtos.request.ReminderRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.ReminderUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.response.ReminderResponseDto;
import com.marcos.personalNotesWebApplication.services.ReminderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reminders")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:4200", "http://localhost:5173"})
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReminderResponseDto> createReminder(
            @Valid @RequestBody ReminderRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reminderService.createReminder(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponseDto> getReminderById(
            @PathVariable UUID id) {
        return ResponseEntity.ok(reminderService.getReminderById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ReminderResponseDto>> getAllReminders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reminderTime") String sortBy) {
        return ResponseEntity.ok(reminderService.getAllReminders(startDate, endDate, page, size, sortBy));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@reminderService.isReminderOwner(#id, principal.username)")
    public ResponseEntity<ReminderResponseDto> updateReminder(
            @PathVariable UUID id,
            @Valid @RequestBody ReminderUpdateDto request) {
        return ResponseEntity.ok(reminderService.updateReminder(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@reminderService.isReminderOwner(#id, principal.username)")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteReminder(@PathVariable UUID id) {
        reminderService.deleteReminder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ReminderResponseDto>> getUserReminders(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reminderTime") String sortBy) {
        return ResponseEntity.ok(reminderService.getUserReminders(userId, page, size, sortBy));
    }

    @GetMapping("/user/{userId}/slice")
    public ResponseEntity<Slice<ReminderResponseDto>> getUserRemindersSlice(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(reminderService.getUserRemindersSlice(userId, page, size));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<ReminderResponseDto>> getUpcomingReminders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reminderTime") String sortBy) {
        return ResponseEntity.ok(reminderService.getUpcomingReminders(page, size, sortBy));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ReminderResponseDto>> searchReminders(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "reminderTime") String sortBy) {
        return ResponseEntity.ok(reminderService.searchReminders(q, page, size, sortBy));
    }

    @PatchMapping("/{id}/complete")
    @PreAuthorize("@reminderService.isReminderOwner(#id, principal.username)")
    public ResponseEntity<ReminderResponseDto> markReminderAsCompleted(
            @PathVariable UUID id) {
        return ResponseEntity.ok(reminderService.markReminderAsCompleted(id));
    }
}
