package com.marcos.personalNotesWebApplication.services.impl;

import com.marcos.personalNotesWebApplication.dtos.request.CalendarEventRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.ReminderRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.CalendarEventResponseDto;
import com.marcos.personalNotesWebApplication.dtos.response.ReminderResponseDto;
import com.marcos.personalNotesWebApplication.entities.CalendarEventEntity;
import com.marcos.personalNotesWebApplication.entities.ReminderEntity;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import com.marcos.personalNotesWebApplication.exceptions.ResourceNotFoundException;
import com.marcos.personalNotesWebApplication.repositories.CalendarEventRepository;
import com.marcos.personalNotesWebApplication.repositories.UserRepository;
import com.marcos.personalNotesWebApplication.services.CalendarEventService;
import com.marcos.personalNotesWebApplication.mapper.CalendarEventMapper;
import com.marcos.personalNotesWebApplication.utils.IsNullOrEmptyUtil;
import com.marcos.personalNotesWebApplication.mapper.ReminderMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CalendarEventServiceImpl implements CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;
    private final CalendarEventMapper calendarEventMapper;
    private final IsNullOrEmptyUtil isNullOrEmptyUtil;
    private final ReminderMapper reminderMapper;
    private final UserRepository userRepository;

    public CalendarEventServiceImpl(CalendarEventRepository calendarEventRepository,
                                    CalendarEventMapper calendarEventMapper,
                                    IsNullOrEmptyUtil isNullOrEmptyUtil,
                                    ReminderMapper reminderMapper,
                                    UserRepository userRepository) {
        this.calendarEventRepository = calendarEventRepository;
        this.calendarEventMapper = calendarEventMapper;
        this.isNullOrEmptyUtil = isNullOrEmptyUtil;
        this.reminderMapper = reminderMapper;
        this.userRepository = userRepository;
    }

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    @Override
    public CalendarEventResponseDto createEvent(CalendarEventRequestDto request) {
        UserEntity user = getCurrentUser();
        var eventEntity = calendarEventMapper.toEntity(request, user);
        var savedEvent = calendarEventRepository.save(eventEntity);
        return calendarEventMapper.toResponse(savedEvent);
    }

    @Override
    public CalendarEventResponseDto getEventById(UUID id) {
        return calendarEventRepository.findById(id)
                .map(calendarEventMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }

    @Override
    public List<CalendarEventResponseDto> getAllEvents(Instant startDate, Instant endDate, int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        return calendarEventRepository.findAllByStartTimeBetween(startDate, endDate, pageable)
                .getContent()
                .stream()
                .map(calendarEventMapper::toResponse)
                .toList();
    }

    @Override
    public CalendarEventResponseDto updateEvent(UUID id, CalendarEventRequestDto request) {
        var existingEvent = calendarEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        
        // Verify ownership
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!existingEvent.getUser().getUsername().equals(currentUsername)) {
            throw new IllegalStateException("You don't have permission to update this event");
        }
        
        calendarEventMapper.updateEntityFromRequest(request, existingEvent);
        CalendarEventEntity entity = calendarEventRepository.save(existingEvent);
        return calendarEventMapper.toResponse(entity);
    }

    @Override
    public void deleteEvent(UUID id) {
        CalendarEventEntity event = calendarEventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        
        // Verify ownership
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getUser().getUsername().equals(currentUsername)) {
            throw new IllegalStateException("You don't have permission to delete this event");
        }
        
        calendarEventRepository.delete(event);
    }

    @Override
    public ReminderResponseDto addReminder(UUID eventId, ReminderRequestDto request) {
        CalendarEventEntity event = calendarEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        // Verify ownership
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getUser().getUsername().equals(currentUsername)) {
            throw new IllegalStateException("You don't have permission to add reminders to this event");
        }

        if (isNullOrEmptyUtil.isNullOrEmpty(request)) {
            throw new IllegalArgumentException("Reminder request cannot be null or empty");
        }

        ReminderEntity reminderEntity = reminderMapper.toEntity(request);
        event.addReminder(reminderEntity);
        calendarEventRepository.save(event);
        return reminderMapper.toResponse(reminderEntity);
    }

    @Override
    public ReminderResponseDto getReminderById(UUID eventId, UUID reminderId) {
        CalendarEventEntity event = calendarEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        // Verify ownership
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getUser().getUsername().equals(currentUsername)) {
            throw new IllegalStateException("You don't have permission to view reminders for this event");
        }

        ReminderEntity reminder = event.getReminders().stream()
                .filter(r -> r.getId().equals(reminderId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + reminderId));

        return reminderMapper.toResponse(reminder);
    }

    @Override
    public List<ReminderResponseDto> getEventReminders(UUID eventId) {
        CalendarEventEntity event = calendarEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        // Verify ownership
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getUser().getUsername().equals(currentUsername)) {
            throw new IllegalStateException("You don't have permission to view reminders for this event");
        }

        return event.getReminders().stream()
                .map(reminderMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteReminder(UUID eventId, UUID reminderId) {
        CalendarEventEntity event = calendarEventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + eventId));

        // Verify ownership
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!event.getUser().getUsername().equals(currentUsername)) {
            throw new IllegalStateException("You don't have permission to delete reminders for this event");
        }

        ReminderEntity reminder = event.getReminders().stream()
                .filter(r -> r.getId().equals(reminderId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + reminderId));

        event.removeReminder(reminder);
        calendarEventRepository.save(event);
    }

    @Override
    public boolean isEventOwner(UUID eventId, String username) {
        return calendarEventRepository.findById(eventId)
                .map(event -> event.getUser().getUsername().equals(username))
                .orElse(false);
    }
}
