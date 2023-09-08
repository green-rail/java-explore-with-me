package ru.practicum.explore.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.error.ErrorMessages;
import ru.practicum.explore.error.exception.DataConflictException;
import ru.practicum.explore.error.exception.EntityNotFoundException;
import ru.practicum.explore.error.exception.ForbiddenOperationException;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.event.model.EventState;
import ru.practicum.explore.request.RequestRepository;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.request.model.RequestStatus;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.explore.user.exception.UserNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getUserRequests(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return requestRepository.findByUserId(user).stream()
                .map(ParticipationRequestDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto addRequest(long userId, long eventId) {

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getEventNotFoundMessage(eventId)));

        if (requestRepository.findByUserIdAndEventId(userId, eventId).isPresent()) {
            throw new DataConflictException(
                    String.format("Request from user(%d) to event(%d) already exists.", userId, eventId));
        }

        if (user.getId().equals(event.getInitiator().getId())) {
            throw new DataConflictException(
                    String.format("Can't create request. User(%d) is owner of the event(%d).", userId, eventId));
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new DataConflictException(
                    String.format("Can't create request. Event(%d) is not published.",  eventId));
        }
        if (event.getParticipantLimit() > 0) {
            var confirmed = requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
            if (confirmed >= event.getParticipantLimit()) {
                throw new DataConflictException(
                        String.format("Can't create request. Event(%d) is full.",  eventId));
            }
        }

        var request = new Request();
        request.setRequester(user);
        request.setEvent(event);
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }
        return ParticipationRequestDto.toDto(requestRepository.save(request));
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        var request = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getRequestByIdNotFoundMessage(requestId)));
        if (request.getRequester().getId() != userId) {
            throw new ForbiddenOperationException("Cannot cancel if you're not an owner.");
        }
        request.setStatus(RequestStatus.CANCELED);
        return ParticipationRequestDto.toDto(requestRepository.save(request));
    }
}
