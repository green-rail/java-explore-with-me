package ru.practicum.explore.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {


    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidArgumentException(final MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder("Некорректный объект: ");
        var errors = e.getAllErrors();
        for (int i = 0; i < errors.size(); i++) {
            message.append(errors.get(i).getDefaultMessage());
            if (i < errors.size() - 1) {
                message.append(" | ");
            }
        }
        return new ApiError(null, message.toString(), "", HttpStatus.BAD_REQUEST);
    }

}
