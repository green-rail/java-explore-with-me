package ru.practicum.explore.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.request.model.RequestStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {

    @JsonFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT)
    private LocalDateTime created;

    private Long event;

    private Long id;

    private Long requester;
    private RequestStatus status;

    public static ParticipationRequestDto toDto(Request request) {
        var dto = new ParticipationRequestDto();
        dto.setCreated(request.getCreated());
        dto.setEvent(request.getEvent().getId());
        dto.setId(request.getId());
        dto.setRequester(request.getRequester().getId());
        dto.setStatus(request.getStatus());

        return dto;
    }


}
