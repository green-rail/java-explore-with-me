package ru.practicum.explore.event.service;

import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventServiceAdmin {
    List<EventFullDto> getEventsAdmin(List<Long> userIds, List<String> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      int from, int size);

    EventFullDto updateEventAdmin(long eventId, UpdateEventAdminRequest eventUpdateDto);


}
