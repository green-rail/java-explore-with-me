package ru.practicum.explore.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.NoHandlerFoundException;
import ru.practicum.explore.error.exception.DataConflictException;
import ru.practicum.explore.error.exception.EntityNotFoundException;
import ru.practicum.explore.error.exception.ForbiddenOperationException;
import ru.practicum.explore.error.exception.InvalidRequestException;
import ru.practicum.explore.user.exception.UserNotFoundException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler (value = {
            HttpMessageNotReadableException.class,
            NoHandlerFoundException.class,
            MethodNotAllowedException.class,
            HttpRequestMethodNotSupportedException.class,
            NumberFormatException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class,
            InvalidRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequest(final Exception e) {
        return new ApiError(null, HttpStatus.BAD_REQUEST, "Incorrectly made request.", e.getMessage());
    }

    @ExceptionHandler (value = {
            ForbiddenOperationException.class,
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenOperation(final Exception e) {
        return new ApiError(null, HttpStatus.FORBIDDEN, "The operation can't be executed.", e.getMessage());
    }

    @ExceptionHandler (value = {
            EntityNotFoundException.class,
            UserNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final Exception e) {
        return new ApiError(null, HttpStatus.NOT_FOUND, "The required object was not found.", e.getMessage());
    }

    @ExceptionHandler (value = {
            DataConflictException.class,
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataConflictException(final Exception e) {
        return new ApiError(null, HttpStatus.CONFLICT, "Integrity constraint has been violated.", e.getMessage());
    }

}
