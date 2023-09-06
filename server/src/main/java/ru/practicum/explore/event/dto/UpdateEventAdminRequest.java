package ru.practicum.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.category.CategoryDto;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.error.exception.InvalidRequestException;
import ru.practicum.explore.event.model.Location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    @NotEmpty
    private String annotation;

    @Positive
    private Long category;

    @NotEmpty
    private String description;

    @JsonFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotEmpty
    private String stateAction;

    @NotEmpty
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

    public enum EventStateAction{
        PUBLISH_EVENT,
        REJECT_EVENT
    }
}
