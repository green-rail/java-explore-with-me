package ru.practicum.explore.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.category.CategoryRepository;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.error.ErrorMessages;
import ru.practicum.explore.error.exception.DataConflictException;
import ru.practicum.explore.error.exception.EntityNotFoundException;
import ru.practicum.explore.error.exception.InvalidRequestException;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.model.EventState;
import ru.practicum.explore.request.RequestRepository;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.request.model.RequestStatus;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.explore.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServicePrivateImpl implements EventServicePrivate {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;


    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getUserEvents(long userId, int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return eventRepository.findByInitiator(user, page).stream()
                .map(EventShortDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto addNewEvent(long userId, NewEventDto eventDto) {
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidRequestException(
                    "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: "
                            + Constants.DEFAULT_DATETIME_FORMATTER.format(eventDto.getEventDate()));
        }
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        var category = categoryRepository.findById(eventDto.getCategory())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getCategoryNotFoundMessage(eventDto.getCategory())));
        var entity = eventDto.toEntity();
        entity.setInitiator(user);
        entity.setCategory(category);
        entity.setState(EventState.PENDING);
        return EventFullDto.toDto(eventRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getUserEvent(long userId, long eventId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getEventNotFoundMessage(eventId)));

        return EventFullDto.toDto(event);
    }

    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest eventDto) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getEventNotFoundMessage(eventId)));

        if (event.getState() == EventState.PUBLISHED) {
            throw new DataConflictException("Only pending or canceled events can be changed");
        }

        if (eventDto.getStateAction() != null) {
            var action = eventDto.getStateActionOrThrow();
            switch (action) {
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                default:
                    throw new InvalidRequestException("Invalid action: " + action);
            }
        }

        if (eventDto.getEventDate() != null) {
            LocalDateTime eventDate = eventDto.getEventDate();
            if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new InvalidRequestException("Cannot set date because it should be at least two hours from now.");
            }
            event.setEventDate(eventDto.getEventDate());
        }

        if (eventDto.getAnnotation() != null) {
            event.setAnnotation(eventDto.getAnnotation());
        }

        if (eventDto.getCategory() != null && !eventDto.getCategory().equals(event.getCategory().getId())) {
            var category = categoryRepository.findById(eventDto.getCategory())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    ErrorMessages.getCategoryNotFoundMessage(eventDto.getCategory())));
            event.setCategory(category);
        }

        if (eventDto.getDescription() != null) {
            event.setDescription(eventDto.getDescription());
        }

        if (eventDto.getLocation() != null) {
            event.setLocation(eventDto.getLocation());
        }

        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }

        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }

        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }

        if (eventDto.getTitle() != null) {
            event.setTitle(eventDto.getTitle());
        }

        return EventFullDto.toDto(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserEventRequests(long userId, long eventId) {
        var requests = requestRepository.findByEventId(eventId);
        return requests.stream()
                .map(ParticipationRequestDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateUserEventRequestStatus(long userId, long eventId,
                                                                       EventRequestStatusUpdateRequest updateRequest) {
        var requests = requestRepository.findAllById(updateRequest.getRequestIds());
        var status = updateRequest.getRequestStatusOrThrow();
        if (status == RequestStatus.REJECTED) {
            for (Request r : requests) {
                if (r.getStatus() == RequestStatus.CONFIRMED) {
                    throw new DataConflictException("Cannot reject an already confirmed request.");
                }
            }
            requests.forEach(r -> r.setStatus(status));
        } else if (status == RequestStatus.CONFIRMED) {
            for (Request r : requests) {
                if (!r.getEvent().isRequestModeration() || r.getEvent().getParticipantLimit() == 0) {
                    r.setStatus(RequestStatus.CONFIRMED);
                    continue;
                }
                var confirmedCount = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
                if (confirmedCount >= r.getEvent().getParticipantLimit()) {
                    r.setStatus(RequestStatus.REJECTED);
                    throw new DataConflictException("Cant accept request.");
                }
                r.setStatus(RequestStatus.CONFIRMED);
            }
        }
        var saved = requestRepository.saveAll(requests);
        return EventRequestStatusUpdateResult.toDto(saved);
    }
}
