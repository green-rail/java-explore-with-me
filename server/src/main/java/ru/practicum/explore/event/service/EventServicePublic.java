package ru.practicum.explore.event.service;

import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EventServicePublic {

    List<EventShortDto> getEventsPublic(String searchText, List<Long> categoriesIds, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        boolean onlyAvailable, String sort,
                                        int from, int size);

    EventFullDto getEventPublic(long id);

}
