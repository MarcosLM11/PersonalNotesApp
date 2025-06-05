package com.marcos.personalNotesWebApplication.services.impl;

import com.marcos.personalNotesWebApplication.dtos.request.LoginRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.UserResponseDto;
import com.marcos.personalNotesWebApplication.entities.UserEntity;
import com.marcos.personalNotesWebApplication.exceptions.ResourceNotFoundException;
import com.marcos.personalNotesWebApplication.mapper.UserMapper;
import com.marcos.personalNotesWebApplication.repositories.UserRepository;
import com.marcos.personalNotesWebApplication.services.LoginService;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public LoginServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public boolean validateCredentials(LoginRequestDto loginRequestDto) {
        //TODO Aquí iría la lógica para validar las credenciales del usuario.
        // Por ahora, retornamos true como ejemplo.
        return true;
    }

    @Override
    public UserResponseDto loginUser(LoginRequestDto loginRequestDto) {
        //TODO Aquí iría la lógica para autenticar al usuario y comprobar la validez del token que nos envían.
        validateCredentials(loginRequestDto);

        UserEntity userEntity = userRepository.findByUsername(loginRequestDto.username()).orElseThrow(
                () -> new ResourceNotFoundException("User not found", "username", loginRequestDto.username())
        );
        return userMapper.toResponse(userEntity);
    }
}
