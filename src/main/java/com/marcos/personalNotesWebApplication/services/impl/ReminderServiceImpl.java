package com.marcos.personalNotesWebApplication.services.impl;

import com.marcos.personalNotesWebApplication.dtos.request.ReminderRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.ReminderUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.response.ReminderResponseDto;
import com.marcos.personalNotesWebApplication.entities.CalendarEventEntity;
import com.marcos.personalNotesWebApplication.entities.ReminderEntity;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import com.marcos.personalNotesWebApplication.exceptions.ResourceNotFoundException;
import com.marcos.personalNotesWebApplication.repositories.CalendarEventRepository;
import com.marcos.personalNotesWebApplication.repositories.ReminderRepository;
import com.marcos.personalNotesWebApplication.repositories.UserRepository;
import com.marcos.personalNotesWebApplication.services.ReminderService;
import com.marcos.personalNotesWebApplication.utils.IsNullOrEmptyUtil;
import com.marcos.personalNotesWebApplication.mapper.ReminderMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final ReminderMapper reminderMapper;
    private final ReminderRepository reminderRepository;
    private final IsNullOrEmptyUtil isNullOrEmptyUtil;
    private final UserRepository userRepository;
    private final CalendarEventRepository calendarEventRepository;

    public ReminderServiceImpl(ReminderMapper reminderMapper, ReminderRepository reminderRepository,
                               IsNullOrEmptyUtil isNullOrEmptyUtil, UserRepository userRepository, CalendarEventRepository calendarEventRepository) {
        this.reminderMapper = reminderMapper;
        this.reminderRepository = reminderRepository;
        this.isNullOrEmptyUtil = isNullOrEmptyUtil;
        this.userRepository = userRepository;
        this.calendarEventRepository = calendarEventRepository;
    }

    @Override
    public ReminderResponseDto createReminder(ReminderRequestDto request) {
        CalendarEventEntity event = calendarEventRepository.getReferenceById(request.eventId());

        if (event == null) {
            throw new ResourceNotFoundException("Calendar event not found with id: " + request.eventId());
        }

        if (isNullOrEmptyUtil.isNullOrEmpty(request)) {
            throw new IllegalArgumentException("Reminder request cannot be null");
        }

        ReminderEntity reminderEntity = reminderMapper.toEntity(request);
        reminderEntity.setEvent(event);
        ReminderEntity savedEntity = reminderRepository.save(reminderEntity);
        return reminderMapper.toResponse(savedEntity);
    }

    @Override
    public ReminderResponseDto getReminderById(UUID id) {
        return reminderRepository.findById(id)
                .map(reminderMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));
    }

    @Override
    public List<ReminderResponseDto> getAllReminders(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        if (startDate == null && endDate == null) {
            return reminderMapper.toResponseList(reminderRepository.findAll());
        } else {
            List<ReminderEntity> reminders = reminderRepository.findAllByReminderTimeBetween(
                    startDate != null ? startDate : LocalDateTime.now(),
                    endDate != null ? endDate : LocalDateTime.now().plusDays(7)
            );
            return reminderMapper.toResponseList(reminders);
        }
    }

    @Override
    public ReminderResponseDto updateReminder(UUID id, ReminderUpdateDto request) {
        if (isNullOrEmptyUtil.isNullOrEmpty(request)) {
            throw new IllegalArgumentException("Reminder request cannot be null");
        }

        ReminderEntity existingReminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));

        reminderMapper.updateEntityFromRequest(request, existingReminder);
        ReminderEntity updatedEntity = reminderRepository.save(existingReminder);
        return reminderMapper.toResponse(updatedEntity);
    }

    @Override
    public void deleteReminder(UUID id) {
        ReminderEntity reminderEntity = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));
        reminderRepository.delete(reminderEntity);
    }

    @Override
    public List<ReminderResponseDto> getUserReminders(UUID userId, int page, int size) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId));
        List<ReminderEntity> reminders = reminderRepository.findAllByCreatedBy(user.getId());

        return reminderMapper.toResponseList(reminders);
    }

    @Override
    public List<ReminderResponseDto> getUpcomingReminders(int page, int size) {
        LocalDateTime now = LocalDateTime.now();
        List<ReminderEntity> reminders = reminderRepository.findAllByReminderTimeAfter(now);
        
        if (reminders.isEmpty()) {
            return Collections.emptyList();
        }
        
        return reminderMapper.toResponseList(reminders);
    }

    @Override
    public ReminderResponseDto markReminderAsCompleted(UUID id) {
        ReminderEntity reminderEntity = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));

        reminderEntity.setSent(true);
        ReminderEntity updatedEntity = reminderRepository.save(reminderEntity);
        return reminderMapper.toResponse(updatedEntity);
    }
}
