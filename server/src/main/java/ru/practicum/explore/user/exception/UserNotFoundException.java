package ru.practicum.explore.user.exception;

import ru.practicum.explore.error.exception.ResponseException;

public class UserNotFoundException extends RuntimeException implements ResponseException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(long id) {
        super(String.format("User with id=%d not found", id));
    }

    @Override
    public String getReason() {
        return "User not found.";
    }
}
