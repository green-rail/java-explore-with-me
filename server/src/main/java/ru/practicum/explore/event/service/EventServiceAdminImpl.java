package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.category.CategoryRepository;
import ru.practicum.explore.client.StatClient;
import ru.practicum.explore.comment.CommentRepository;
import ru.practicum.explore.error.ErrorMessages;
import ru.practicum.explore.error.exception.DataConflictException;
import ru.practicum.explore.error.exception.EntityNotFoundException;
import ru.practicum.explore.error.exception.InvalidRequestException;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequest;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventState;
import ru.practicum.explore.request.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventServiceAdminImpl implements EventServiceAdmin {

    private final EventRepository eventRepository;
    private final StatClient statisticsClient;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;


    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getEventsAdmin(List<Long> userIds, List<String> states, List<Long> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                             int from, int size) {

        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
            rangeEnd = rangeStart.plusYears(100);
        }

        if (rangeEnd.isBefore(rangeStart)) {
            throw new InvalidRequestException("Invalid time range arguments.");
        }
        Set<EventState> eventStates = null;
        if (states != null) {
            eventStates = states.stream()
                    .map(EventServiceAdminImpl::parseEventState)
                    .collect(Collectors.toSet());
        }

        PageRequest page = PageRequest.of(from / size, size);
        var events = eventRepository
                .findByUserIdsStatesCategories(userIds, eventStates, categories, rangeStart, rangeEnd, page);

        List<EventFullDto> eventDtos = events.getContent().stream()
                .map(EventFullDto::toDto)
                .collect(Collectors.toList());
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

        if (eventUpdateDto.getStateAction() != null) {
            var action = eventUpdateDto.getStateActionOrThrow();
            updateEventState(eventEntity, eventUpdateDto, action);
        }

        if (eventUpdateDto.getEventDate() != null) {
            if (eventUpdateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new InvalidRequestException("Event date should be before now.");
            }
            eventEntity.setEventDate(eventUpdateDto.getEventDate());
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
                    throw new DataConflictException("Cannot publish the event because it's not in the right state: PENDING");
                }
                LocalDateTime eventDate = dto.getEventDate();
                if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(1))) {
                    throw new DataConflictException("Cannot publish the event because the event date should be at least an hour after publication.");
                }
                eventEntity.setState(EventState.PUBLISHED);
                eventEntity.setPublishedOn(LocalDateTime.now());
                break;

            case REJECT_EVENT:
                if (currentState != EventState.PENDING) {
                    throw new DataConflictException("Cannot reject the event because it's not in the right state: PENDING");
                }
                eventEntity.setState(EventState.CANCELED);
                break;

            default:
                throw new IllegalArgumentException("Invalid action: " + action);
        }
    }

    @Override
    public void deleteComment(long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new EntityNotFoundException(ErrorMessages.getCommentNotFoundMessage(commentId));
        }
        commentRepository.deleteById(commentId);
    }
}
