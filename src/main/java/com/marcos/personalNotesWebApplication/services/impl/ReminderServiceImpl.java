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
import com.marcos.personalNotesWebApplication.utils.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final ReminderMapper reminderMapper;
    private final ReminderRepository reminderRepository;
    private final IsNullOrEmptyUtil isNullOrEmptyUtil;
    private final UserRepository userRepository;
    private final CalendarEventRepository calendarEventRepository;
    private final PageableUtils pageableUtils;

    public ReminderServiceImpl(ReminderMapper reminderMapper, ReminderRepository reminderRepository,
                               IsNullOrEmptyUtil isNullOrEmptyUtil, UserRepository userRepository,
                               CalendarEventRepository calendarEventRepository, PageableUtils pageableUtils) {
        this.reminderMapper = reminderMapper;
        this.reminderRepository = reminderRepository;
        this.isNullOrEmptyUtil = isNullOrEmptyUtil;
        this.userRepository = userRepository;
        this.calendarEventRepository = calendarEventRepository;
        this.pageableUtils = pageableUtils;
    }

    @Override
    public ReminderResponseDto createReminder(ReminderRequestDto request) {
        CalendarEventEntity event = calendarEventRepository.getReferenceById(request.eventId());

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
    public Page<ReminderResponseDto> getAllReminders(LocalDateTime startDate, LocalDateTime endDate, int page, int size, String sortBy) {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy);
        
        Page<ReminderEntity> remindersPage;
        if (startDate == null && endDate == null) {
            remindersPage = reminderRepository.findAll(pageable);
        } else {
            LocalDateTime start = startDate != null ? startDate : LocalDateTime.now();
            LocalDateTime end = endDate != null ? endDate : LocalDateTime.now().plusDays(7);
            remindersPage = reminderRepository.findAllByReminderTimeBetween(start, end, pageable);
        }
        
        return remindersPage.map(reminderMapper::toResponse);
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
    public Page<ReminderResponseDto> getUserReminders(UUID userId, int page, int size, String sortBy) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy);
        Page<ReminderEntity> remindersPage = reminderRepository.findAllByUserId(userId, pageable);
        
        return remindersPage.map(reminderMapper::toResponse);
    }

    @Override
    public Slice<ReminderResponseDto> getUserRemindersSlice(UUID userId, int page, int size) {
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User not found with id: " + userId));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "reminderTime"));
        Slice<ReminderEntity> remindersSlice = reminderRepository.findAllByUserIdSlice(userId, pageable);
        
        return remindersSlice.map(reminderMapper::toResponse);
    }

    @Override
    public Page<ReminderResponseDto> getUpcomingReminders(int page, int size, String sortBy) {
        LocalDateTime now = LocalDateTime.now();
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy);
        Page<ReminderEntity> remindersPage = reminderRepository.findAllByReminderTimeAfter(now, pageable);
        
        return remindersPage.map(reminderMapper::toResponse);
    }

    @Override
    public Page<ReminderResponseDto> searchReminders(String query, int page, int size, String sortBy) {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy);
        Page<ReminderEntity> remindersPage = reminderRepository.searchByEventTitleOrDescription(query, pageable);
        
        return remindersPage.map(reminderMapper::toResponse);
    }

    @Override
    public ReminderResponseDto markReminderAsCompleted(UUID id) {
        ReminderEntity reminderEntity = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));

        reminderEntity.setSent(true);
        ReminderEntity updatedEntity = reminderRepository.save(reminderEntity);
        return reminderMapper.toResponse(updatedEntity);
    }

    @Override
    public boolean isReminderOwner(UUID reminderId, String username) {
        return reminderRepository.existsByIdAndUsername(reminderId, username);
    }
}
