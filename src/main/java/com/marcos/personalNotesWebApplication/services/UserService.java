package com.marcos.personalNotesWebApplication.services;

import com.marcos.personalNotesWebApplication.dtos.request.UserRequestDto;
import com.marcos.personalNotesWebApplication.dtos.response.UserResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface UserService {

    /**
     * Creates a new user.
     *
     * @param request the user request data transfer object containing user details
     * @return the created user response data transfer object
     */
    UserResponseDto createUser(@Valid UserRequestDto request);

    /**
     * Retrieves a user by their ID.
     *
     * @param id the UUID of the user
     * @return the user response data transfer object
     */
    UserResponseDto getUserById(UUID id);

    /**
     * Retrieves all users with pagination and sorting.
     *
     * @param page the page number to retrieve
     * @param size the number of users per page
     * @param sortBy the field to sort by
     * @return a list of user response data transfer objects
     */
    List<UserResponseDto> getAllUsers(int page, int size, String sortBy);

    /**
     * Updates an existing user.
     *
     * @param id the UUID of the user to update
     * @param request the user request data transfer object containing updated user details
     * @return the updated user response data transfer object
     */
    UserResponseDto updateUser(UUID id, @Valid UserRequestDto request);

    /**
     * Deletes a user by their ID.
     *
     * @param id the UUID of the user to delete
     */
    void deleteUser(UUID id);
}
