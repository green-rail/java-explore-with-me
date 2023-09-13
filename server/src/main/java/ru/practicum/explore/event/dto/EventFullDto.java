package ru.practicum.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventState;
import ru.practicum.explore.event.model.Location;
import ru.practicum.explore.user.dto.UserShortDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    @NotNull
    @NotEmpty
    private String annotation;

    @NotNull
    private CategoryDto category;

    private Integer confirmedRequests;

    @JsonFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT)
    private LocalDateTime createdOn;

    private String description;

    @NotNull
    @JsonFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT)
    private LocalDateTime eventDate;

    private Long id;

    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;

    @NotNull
    private Boolean paid;
    private int participantLimit;

    @JsonFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT)
    private LocalDateTime publishedOn;

    private boolean requestModeration = true;

    private EventState state;

    @NotNull
    @NotEmpty
    private String title;

    private Long views;

    private List<CommentDto> comments;

    public static EventFullDto toDto(Event event) {
        var dto = new EventFullDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryDto.toDto(event.getCategory()));
        dto.setConfirmedRequests(event.getRequestCount());
        dto.setCreatedOn(event.getCreatedOn());
        dto.setDescription(event.getDescription());
        dto.setEventDate(event.getEventDate());
        dto.setId(event.getId());
        dto.setInitiator(UserShortDto.toDto(event.getInitiator()));
        dto.setLocation(event.getLocation());
        dto.setPaid(event.isPaid());
        dto.setParticipantLimit(event.getParticipantLimit());
        dto.setPublishedOn(event.getPublishedOn());
        dto.setRequestModeration(event.isRequestModeration());
        dto.setState(event.getState());
        dto.setTitle(event.getTitle());

        dto.setComments(event.getComments() == null ?
                Collections.emptyList() :
                event.getComments().stream()
                        .map(CommentDto::toDto)
                        .collect(Collectors.toList())
        );

        return dto;
    }
}
