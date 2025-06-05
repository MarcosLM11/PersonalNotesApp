package com.marcos.personalNotesWebApplication.services;

import com.marcos.personalNotesWebApplication.dtos.request.LoginRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.UserResponseDto;

public interface LoginService {

    /**
     * Validates the user's login credentials.
     *
     * @param loginRequestDto the username and password of the user
     * @return true if the credentials are valid, false otherwise
     */
    boolean validateCredentials(LoginRequestDto loginRequestDto);

    /**
     * Logs in a user with the provided credentials.
     *
     * @param loginRequestDto the username and password of the user
     * @return a message indicating success or failure
     */
    UserResponseDto loginUser(LoginRequestDto loginRequestDto);
}
