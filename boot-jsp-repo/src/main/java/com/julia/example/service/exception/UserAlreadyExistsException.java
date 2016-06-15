package com.julia.example.service.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(final String message) {
        super(message);
    }

}
