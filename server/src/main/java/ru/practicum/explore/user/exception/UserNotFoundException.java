package ru.practicum.explore.user.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(long id) {
        super(String.format("Пользователь с id [%d] не найден", id));
    }

}
