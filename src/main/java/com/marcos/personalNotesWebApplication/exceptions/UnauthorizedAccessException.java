package com.marcos.personalNotesWebApplication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedAccessException extends RuntimeException {
    
    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String resourceType, String resourceId, String username) {
        super(String.format("User '%s' is not authorized to access %s with id: %s", username, resourceType, resourceId));
    }
} 