package ru.practicum.explore.error.exception;

public class ForbiddenOperationException extends RuntimeException implements ResponseException {
    private String reason = "For the requested operation the conditions are not met.";

    public ForbiddenOperationException(String message) {
        super(message);
    }

    public ForbiddenOperationException(String message, String reason) {
        super(message);
        this.reason = reason;
    }

    @Override
    public String getReason() {
        return reason;
    }
}
