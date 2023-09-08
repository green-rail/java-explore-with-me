package ru.practicum.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.error.ErrorMessages;
import ru.practicum.explore.error.exception.InvalidRequestException;
import ru.practicum.explore.event.model.Location;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    @Size(min = 20, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 2000, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String annotation;

    @Positive
    private Long category;

    @Size(min = 20, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 7000, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String description;

    @JsonFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private String stateAction;

    @Size(min = 3, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 120, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String title;

    public EventStateAction getStateActionOrThrow() throws InvalidRequestException {
        if (stateAction == null) {
            return null;
        }
        try {
            return EventStateAction.valueOf(stateAction.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid state action: " + stateAction);
        }
    }

    public enum EventStateAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }
}
