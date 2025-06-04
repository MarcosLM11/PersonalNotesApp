package com.marcos.personalNotesWebApplication.services.impl;

import com.marcos.personalNotesWebApplication.dtos.request.UserRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.PasswordChangeRequestDto;
import com.marcos.personalNotesWebApplication.dtos.request.UserUpdateDto;
import com.marcos.personalNotesWebApplication.dtos.response.UserResponseDto;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import com.marcos.personalNotesWebApplication.exceptions.ResourceNotFoundException;
import com.marcos.personalNotesWebApplication.exceptions.UnauthorizedAccessException;
import com.marcos.personalNotesWebApplication.repositories.UserRepository;
import com.marcos.personalNotesWebApplication.services.UserService;
import com.marcos.personalNotesWebApplication.mapper.UserMapper;
import com.marcos.personalNotesWebApplication.utils.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PageableUtils pageableUtils;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository,
                           PasswordEncoder passwordEncoder, PageableUtils pageableUtils) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pageableUtils = pageableUtils;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto request) {
        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userRepository.save(userEntity);
        return userMapper.toResponse(userEntity);
    }

    @Override
    public UserResponseDto getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with id: " + id)
                );
    }

    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size, String sortBy) {
        Pageable pageable = pageableUtils.createPageable(page, size, sortBy);
        Page<UserEntity> usersPage = userRepository.findAll(pageable); // Implement pagination and sorting as needed
        return usersPage.map(userMapper::toResponse);
    }

    @Override
    public UserResponseDto updateUser(UUID id, UserUpdateDto request) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        userMapper.updateEntityFromRequest(request, userEntity);
        userRepository.save(userEntity);
        return userMapper.toResponse(userEntity);
    }

    @Override
    public void deleteUser(UUID id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(userEntity);
    }

    @Override
    public void changePassword(UUID id, PasswordChangeRequestDto passwordChangeRequest) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // Verify the current password
        if (!passwordEncoder.matches(passwordChangeRequest.currentPassword(), userEntity.getPassword())) {
            throw new UnauthorizedAccessException("Current password is incorrect");
        }

        // Update password
        userEntity.setPassword(passwordEncoder.encode(passwordChangeRequest.newPassword()));
        userRepository.save(userEntity);
    }
}
