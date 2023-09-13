package ru.practicum.explore.error;

public class ErrorMessages {
    public static String getEventNotFoundMessage(long id) {
        return String.format("Event with id=%d was not found", id);
    }

    public static String getCategoryNotFoundMessage(long id) {
        return String.format("Category with id=%d was not found", id);
    }

    public static String getRequestNotFoundMessage(long userId, long eventId) {
        return String.format("Request with user_id=%d and event_id=%d was not found", userId, eventId);
    }

    public static String getRequestByIdNotFoundMessage(long requestId) {
        return String.format("Request with id=%d was not found", requestId);
    }

    public static String getCompilationNotFoundMessage(long compilationId) {
        return String.format("Compilation with id=%d was not found",  compilationId);
    }

    public static final String STRING_TOO_SHORT_MESSAGE = "String too short.";
    public static final String STRING_TOO_LONG_MESSAGE = "String too long.";

    public static String getCommentNotFoundMessage(long id) {
        return String.format("Comment with id=%d was not found", id);
    }
}
