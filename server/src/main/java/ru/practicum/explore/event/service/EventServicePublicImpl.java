package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.client.StatClient;
import ru.practicum.explore.error.exception.EntityNotFoundException;
import ru.practicum.explore.error.exception.InvalidRequestException;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventState;
import ru.practicum.explore.request.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventServicePublicImpl implements EventServicePublic {


    private final EventRepository eventRepository;
    private final StatClient statisticsClient;
    private final RequestRepository requestRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsPublic(String searchText, List<Long> categoriesIds, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               boolean onlyAvailable, String sort,
                                               int from, int size) {
        if (searchText.isBlank()) {
            searchText = null;
        }
        boolean eventDateSort = false;
        if ("EVENT_DATE".equalsIgnoreCase(sort)) {
            eventDateSort = true;
        } else if (!"VIEWS".equalsIgnoreCase(sort)) {
            throw new InvalidRequestException("Invalid sorting argument. Can only be one of [EVENT_DATE, VIEWS] or empty");
        }
        boolean useTimeRange = isRangeOrThrow(rangeStart, rangeEnd);
        LocalDateTime start;
        LocalDateTime end;
        if (useTimeRange) {
            start = rangeStart;
            end = rangeEnd;
        } else {
            start = LocalDateTime.now();
            end = start.plusYears(100);
        }

        PageRequest page = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "eventDate"));
        Page<Event> events;
        if (paid == null) {
            if (onlyAvailable) {
                events = eventRepository.findBySearchOnlyAvailable(EventState.PUBLISHED, searchText, start, end, page);
            } else {
                events = eventRepository.findBySearch(EventState.PUBLISHED, searchText, start, end, page);
            }
        } else {
            if (onlyAvailable) {
                events = eventRepository.findBySearchAndPaidOnlyAvailable(EventState.PUBLISHED, searchText, paid, start, end, page);
            } else {
                events = eventRepository.findBySearchAndPaid(EventState.PUBLISHED, searchText, paid, start, end, page);
            }
        }

        List<EventShortDto> eventDtos = events.getContent().stream()
                .map(EventShortDto::toDto)
                .collect(Collectors.toList());

        return eventDtos;
    }

    private static boolean isRangeOrThrow(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart == null && rangeEnd == null) {
            return false;
        }

        if (rangeStart == null || rangeEnd == null || rangeStart.isAfter(rangeEnd)) {
            throw new InvalidRequestException("Invalid time range arguments.");
        }

        return true;
    }


    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventPublic(long id) {
        var event = eventRepository.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with id=%d was not found", id)));

        var dto = EventFullDto.toDto(event);
        var url = "/events/" + id;
        var responseEntity = statisticsClient.getStats(LocalDateTime.now().minusYears(100), LocalDateTime.now().plusYears(100),
                new String[] {url}, true);
        long hitCount = 0;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            try {
                var hitsMap = (ArrayList<Map>)responseEntity.getBody();
                hitCount = Long.parseLong(String.valueOf(hitsMap.get(0).getOrDefault("hits", "0")));
            } catch (Exception ignored) {
                log.warn("Unexpected error while parsing hit count.");
            }
        }
        dto.setViews(hitCount);
        return dto;
    }
}
