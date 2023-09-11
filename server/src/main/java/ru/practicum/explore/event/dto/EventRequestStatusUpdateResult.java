package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.request.model.RequestStatus;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateResult {

    private List<ParticipationRequestDto> confirmedRequests;

    private List<ParticipationRequestDto> rejectedRequests;

    public static EventRequestStatusUpdateResult toDto(List<Request> requests) {
        var dto = new EventRequestStatusUpdateResult();
        dto.confirmedRequests = new ArrayList<>();
        dto.rejectedRequests = new ArrayList<>();
        for (var r :requests) {
            if (r.getStatus() == RequestStatus.CONFIRMED) {
                dto.confirmedRequests.add(ParticipationRequestDto.toDto(r));
            } else {
                dto.rejectedRequests.add(ParticipationRequestDto.toDto(r));
            }
        }
        return dto;
    }
}
