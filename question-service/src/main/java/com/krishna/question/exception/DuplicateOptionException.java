package com.krishna.question.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception for indicating not found conflicts.
 */
@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DuplicateOptionException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified error
     * message.
     *
     * @param message The error message.
     */
    public DuplicateOptionException(final String message) {
        super(message);
    }
}
