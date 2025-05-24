package com.marcos.personalNotesWebApplication.services.impl;

import com.marcos.personalNotesWebApplication.dtos.request.UserRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.UserResponseDto;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import com.marcos.personalNotesWebApplication.exceptions.ResourceNotFoundException;
import com.marcos.personalNotesWebApplication.repositories.UserRepository;
import com.marcos.personalNotesWebApplication.services.UserService;
import com.marcos.personalNotesWebApplication.utils.IsNullOrEmptyUtil;
import com.marcos.personalNotesWebApplication.mapper.UserMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final IsNullOrEmptyUtil isNullOrEmptyUtil;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository,
                           IsNullOrEmptyUtil isNullOrEmptyUtil) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.isNullOrEmptyUtil = isNullOrEmptyUtil;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto request) {
        UserEntity userEntity = userMapper.toEntity(request);
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
    public List<UserResponseDto> getAllUsers(int page, int size, String sortBy) {

        List<UserEntity> users = userRepository.findAll(); // Implement pagination and sorting as needed

        if (isNullOrEmptyUtil.isNullOrEmpty(users)) {
            throw new ResourceNotFoundException("No users found");
        }

        return users.stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponseDto updateUser(UUID id, UserRequestDto request) {
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
}
