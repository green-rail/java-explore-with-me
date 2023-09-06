package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.category.CategoryRepository;
import ru.practicum.explore.client.StatClient;
import ru.practicum.explore.common.dto.ViewStatsDto;
import ru.practicum.explore.error.exception.EntityNotFoundException;
import ru.practicum.explore.error.exception.ForbiddenOperationException;
import ru.practicum.explore.error.exception.InvalidRequestException;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequest;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceAdminImpl implements EventServiceAdmin {

    private final EventRepository eventRepository;
    private final StatClient statisticsClient;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsAdmin(List<Long> userIds, List<String> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             int from, int size) {


        if (rangeEnd.isBefore(rangeStart)) {
            throw new InvalidRequestException("Invalid time range arguments.");
        }
        Set<EventState> eventStates = states.stream()
                .map(EventServiceAdminImpl::parseEventState)
                .collect(Collectors.toSet());

        PageRequest page = PageRequest.of(from / size, size);
        var events = eventRepository
                .findByUserIdsStatesCategories(userIds, eventStates, categories, rangeStart, rangeEnd);


        List<EventFullDto> eventDtos = events.getContent().stream()
                .map(EventFullDto::toDto)
                .collect(Collectors.toList());
        var eventsUrls = new String[eventDtos.size()];
        String eventsBaseUrl = "/events/";
        for (int i = 0; i < eventDtos.size(); i++) {
            eventsUrls[i] = eventsBaseUrl + eventDtos.get(i).getId().toString();
        }
        var responseEntity = statisticsClient.getStats(rangeStart, rangeEnd, eventsUrls, false);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {

            List<ViewStatsDto> viewStatsList = (List<ViewStatsDto>) responseEntity.getBody();
            if ( viewStatsList != null && viewStatsList.size() == eventDtos.size()) {
                for (int i = 0; i < viewStatsList.size(); i++) {
                    eventDtos.get(i).setViews(viewStatsList.get(i).getHits());
                }
            }
        }
        return eventDtos;
    }

    private static EventState parseEventState(String state) {
        try {
            return EventState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Invalid state: " + state);
        }
    }

    @Override
    public EventFullDto updateEventAdmin(long eventId, UpdateEventAdminRequest eventUpdateDto) {
        var eventEntity = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Event with id=%d was not found", eventId)));

        if (eventUpdateDto.getEventDate() != null) {
            eventEntity.setEventDate(eventUpdateDto.getEventDate());
        }

        if (eventUpdateDto.getStateAction() != null) {
            var action = eventUpdateDto.getStateActionOrThrow();
            updateEventState(eventEntity, eventUpdateDto, action);
        }

        if (eventUpdateDto.getAnnotation() != null) {
            eventEntity.setAnnotation(eventUpdateDto.getAnnotation());
        }

        if (eventUpdateDto.getCategory() != null) {
            if (!eventUpdateDto.getCategory().equals(eventEntity.getCategory().getId())) {
                var category = categoryRepository.findById(eventUpdateDto.getCategory())
                        .orElseThrow(() -> new EntityNotFoundException(String.format(
                                "Category with id=%d was not found", eventUpdateDto.getCategory())));
                eventEntity.setCategory(category);
            }
        }

        if (eventUpdateDto.getDescription() != null) {
            eventEntity.setDescription(eventUpdateDto.getDescription());
        }


        if (eventUpdateDto.getLocation() != null) {
            eventEntity.setLocation(eventUpdateDto.getLocation());
        }

        if (eventUpdateDto.getPaid() != null) {
            eventEntity.setPaid(eventUpdateDto.getPaid());
        }

        if (eventUpdateDto.getParticipantLimit() != null) {
            eventEntity.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }

        if (eventUpdateDto.getRequestModeration() != null) {
            eventEntity.setRequestModeration(eventUpdateDto.getRequestModeration());
        }

        if (eventUpdateDto.getTitle() != null) {
            eventEntity.setTitle(eventUpdateDto.getTitle());
        }

        return EventFullDto.toDto(eventRepository.save(eventEntity));
    }


    private static void updateEventState(Event eventEntity, UpdateEventAdminRequest dto, UpdateEventAdminRequest.EventStateAction action) {
        EventState currentState = eventEntity.getState();

        switch (action) {
            case PUBLISH_EVENT:
                if (currentState != EventState.PENDING) {
                    throw new ForbiddenOperationException("Cannot publish the event because it's not in the right state: PENDING");
                }
                LocalDateTime eventDate = eventEntity.getEventDate();
                if (eventDate.isAfter(LocalDateTime.now().plusHours(1))) {
                    throw new ForbiddenOperationException("Cannot publish the event because the date should be at least an hour after publication.");
                }
                eventEntity.setState(EventState.PUBLISHED);
                break;

            case REJECT_EVENT:
                if (currentState != EventState.PENDING) {
                    throw new ForbiddenOperationException("Cannot reject the event because it's not in the right state: PENDING");
                }
                eventEntity.setState(EventState.CANCELED);
                break;

            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }

    }


}
