package ru.practicum.explore.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.practicum.explore.common.Constants;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ApiError {
    private final List<String> errors;
    private final String message;
    private final String reason;
    private final String status;
    private final String timestamp;

    public ApiError(List<String> errors, String message, String reason, HttpStatus status) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status.name();
        this.timestamp = Constants.getDefaultFormatter().format(LocalDateTime.now());
    }

}
