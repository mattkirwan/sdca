package com.sdca.api.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super(String.format("User ID:%s not found", userId));
    }
}
