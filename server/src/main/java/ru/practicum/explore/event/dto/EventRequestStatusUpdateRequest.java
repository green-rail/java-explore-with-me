package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.request.model.RequestStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    private List<Long> requestIds;
    private String status;

    public RequestStatus getRequestStatusOrThrow() {
        if (status == null) {
            return null;
        }
        try {
            return RequestStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
