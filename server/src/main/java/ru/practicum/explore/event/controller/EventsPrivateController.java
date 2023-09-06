package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.service.EventServicePrivate;
import ru.practicum.explore.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventsPrivateController {

    private final EventServicePrivate eventService;

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable @Positive long userId,
                                             @RequestParam(defaultValue = "0")  @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size,
                                             HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable @Positive long userId,
                                 @RequestBody @Valid NewEventDto newEventDto,
                                 HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.addNewEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable @Positive long userId,
                                      @PathVariable @Positive long eventId,
                                      HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable @Positive long userId,
                                     @PathVariable @Positive long eventId,
                                     @RequestBody @Valid UpdateEventUserRequest eventUpdateDto,
                                     HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.updateEvent(userId, eventId, eventUpdateDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable @Positive long userId,
                                                              @PathVariable @Positive long eventId,
                                                              HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventRequestStatus(@PathVariable @Positive long userId,
                                                                       @PathVariable @Positive long eventId,
                                                                       @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest,
                                                                       HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.updateUserEventRequestStatus(userId, eventId, updateRequest);

    }
}