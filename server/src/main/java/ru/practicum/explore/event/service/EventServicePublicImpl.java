package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.client.StatClient;
import ru.practicum.explore.common.dto.ViewStatsDto;
import ru.practicum.explore.error.exception.EntityNotFoundException;
import ru.practicum.explore.error.exception.InvalidRequestException;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventState;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServicePublicImpl implements EventServicePublic {


    private final EventRepository eventRepository;
    private final StatClient statisticsClient;

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsPublic(String searchText, List<Long> categoriesIds, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                               boolean onlyAvailable, String sort,
                                               int from, int size) {
        if (searchText.isBlank()) {
            return Collections.emptyList();
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
                events = eventRepository.findBySearchOnlyAvailable(searchText, start, end, page);
            } else {
                events = eventRepository.findBySearch(searchText, start, end, page);
            }
        } else {
            if (onlyAvailable) {
                events = eventRepository.findBySearchAndPaidOnlyAvailable(searchText, paid, start, end, page);
            } else {
                events = eventRepository.findBySearchAndPaid(searchText, paid, start, end, page);
            }
        }

        List<EventShortDto> eventDtos = events.getContent().stream()
                .map(EventShortDto::toDto)
                .collect(Collectors.toList());
        var eventsUrls = new String[eventDtos.size()];
        String eventsBaseUrl = "/events/";
        for (int i = 0; i < eventDtos.size(); i++) {
            eventsUrls[i] = eventsBaseUrl + eventDtos.get(i).getId().toString();
        }
        var responseEntity = statisticsClient.getStats(start, end, eventsUrls, false);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            List<ViewStatsDto> viewStatsList = (List<ViewStatsDto>) responseEntity.getBody();
            if ( viewStatsList != null && viewStatsList.size() == eventDtos.size()) {
                for (int i = 0; i < viewStatsList.size(); i++) {
                    eventDtos.get(i).setViews(viewStatsList.get(i).getHits());
                }
            }
        }
        if (!eventDateSort) {
            eventDtos.sort(Comparator.comparingLong(EventShortDto::getViews));
        }
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
        var event = eventRepository.findByIdAndStateLike(id, EventState.PUBLISHED.name())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with id=%d was not found", id)));
        return EventFullDto.toDto(event);
    }
}
