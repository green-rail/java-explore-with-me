package ru.practicum.explore.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.practicum.explore.category.CategoryDto;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventState;
import ru.practicum.explore.event.model.Location;
import ru.practicum.explore.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;

    @JsonFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT)
    private LocalDateTime eventDate;

    private Long id;
    private UserShortDto initiator;

    private boolean paid;

    private String title;
    private Long views;


    public static EventShortDto toDto(Event event) {
        var dto = new EventShortDto();
        dto.setAnnotation(event.getAnnotation());
        dto.setCategory(CategoryDto.toDto(event.getCategory()));
        dto.setConfirmedRequests(event.getConfirmedRequests());
        dto.setEventDate(event.getEventDate());
        dto.setId(event.getId());
        dto.setInitiator(UserShortDto.toDto(event.getInitiator()));
        dto.setPaid(event.isPaid());
        dto.setTitle(event.getTitle());
        return dto;
    }

}
