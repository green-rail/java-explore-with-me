package ru.practicum.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.error.ErrorMessages;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.Location;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotEmpty
    @NotNull
    @Size(min = 20, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 2000, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String annotation;

    @Positive
    private long category;

    @NotEmpty
    @NotNull
    @Size(min = 20, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 7000, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String description;

    @NotNull
    @JsonFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    private boolean paid = false;

    private int participantLimit = 0;
    private boolean requestModeration = true;

    @NotEmpty
    @NotNull
    @Size(min = 3, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 120, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String title;

    public Event toEntity() {
        var event = new Event();
        event.setAnnotation(annotation);
        event.setDescription(description);
        event.setEventDate(eventDate);
        event.setLocation(location);
        event.setPaid(paid);
        event.setParticipantLimit(participantLimit);
        event.setRequestModeration(requestModeration);
        event.setTitle(title);
        return event;
    }
}
