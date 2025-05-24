package com.marcos.personalNotesWebApplication.services.impl;

import com.marcos.personalNotesWebApplication.dtos.request.ReminderRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.ReminderResponseDto;
import com.marcos.personalNotesWebApplication.entities.ReminderEntity;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import com.marcos.personalNotesWebApplication.exceptions.ResourceNotFoundException;
import com.marcos.personalNotesWebApplication.repositories.ReminderRepository;
import com.marcos.personalNotesWebApplication.repositories.UserRepository;
import com.marcos.personalNotesWebApplication.services.ReminderService;
import com.marcos.personalNotesWebApplication.utils.IsNullOrEmptyUtil;
import com.marcos.personalNotesWebApplication.mapper.ReminderMapper;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final ReminderMapper reminderMapper;
    private final ReminderRepository reminderRepository;
    private final IsNullOrEmptyUtil isNullOrEmptyUtil;
    private final UserRepository userRepository;

    public ReminderServiceImpl(ReminderMapper reminderMapper, ReminderRepository reminderRepository,
                                IsNullOrEmptyUtil isNullOrEmptyUtil, UserRepository userRepository) {
        this.reminderMapper = reminderMapper;
        this.reminderRepository = reminderRepository;
        this.isNullOrEmptyUtil = isNullOrEmptyUtil;
        this.userRepository = userRepository;
    }

    @Override
    public ReminderResponseDto createReminder(ReminderRequestDto request) {
        if (isNullOrEmptyUtil.isNullOrEmpty(request)) {
            throw new IllegalArgumentException("Reminder request cannot be null");
        }
        var reminderEntity = reminderMapper.toEntity(request);
        var savedEntity = reminderRepository.save(reminderEntity);
        return reminderMapper.toResponse(savedEntity);
    }

    @Override
    public ReminderResponseDto getReminderById(UUID id) {
        return reminderRepository.findById(id)
                .map(reminderMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));
    }

    @Override
    public List<ReminderResponseDto> getAllReminders(Instant startDate, Instant endDate, int page, int size) {
        List<ReminderEntity> reminders = reminderRepository.findAllByReminderTimeBetween(startDate, endDate);
        if (isNullOrEmptyUtil.isNullOrEmpty(reminders)) {
            throw new ResourceNotFoundException("No reminders found in the specified date range");
        }
        return reminderMapper.toResponseList(reminders);
    }

    @Override
    public ReminderResponseDto updateReminder(UUID id, ReminderRequestDto request) {
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
        List<ReminderEntity> reminders = reminderRepository.findAllByCreatedBy(user.getUsername());

        if (isNullOrEmptyUtil.isNullOrEmpty(reminders)) {
            throw new ResourceNotFoundException("No reminders found for user: " + user.getUsername());
        }

        return reminderMapper.toResponseList(reminders);
    }

    @Override
    public List<ReminderResponseDto> getUpcomingReminders(int page, int size) {
        Instant now = Instant.now();
        List<ReminderEntity> reminders = reminderRepository.findAllByReminderTimeAfter(now);

        if (isNullOrEmptyUtil.isNullOrEmpty(reminders)) {
            throw new ResourceNotFoundException("No upcoming reminders found");
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
