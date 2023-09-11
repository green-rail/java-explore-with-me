package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.service.EventServicePrivate;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.NewCommentRequest;

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
    public List<EventShortDto> getUserEvents(@PathVariable @PositiveOrZero long userId,
                                             @RequestParam(defaultValue = "0")  @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size,
                                             HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.getUserEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable @PositiveOrZero long userId,
                                 @RequestBody @Valid NewEventDto newEventDto,
                                 HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        var event = eventService.addNewEvent(userId, newEventDto);
        log.debug("ADDED EVENT " + event);
        return event;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable @PositiveOrZero long userId,
                                      @PathVariable @PositiveOrZero long eventId,
                                      HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable @PositiveOrZero long userId,
                                     @PathVariable @PositiveOrZero long eventId,
                                     @RequestBody @Valid UpdateEventUserRequest eventUpdateDto,
                                     HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.updateEvent(userId, eventId, eventUpdateDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getUserEventRequests(@PathVariable @PositiveOrZero long userId,
                                                              @PathVariable @PositiveOrZero long eventId,
                                                              HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.getUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateUserEventRequestStatus(@PathVariable @PositiveOrZero long userId,
                                                                       @PathVariable @PositiveOrZero long eventId,
                                                                       @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest,
                                                                       HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.updateUserEventRequestStatus(userId, eventId, updateRequest);
    }

    @PostMapping("/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto postComment(@PathVariable @PositiveOrZero long userId,
                                  @PathVariable @PositiveOrZero long eventId,
                                  @RequestBody @Valid NewCommentRequest commentRequest,
                                  HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.postComment(userId, eventId, commentRequest);
    }

    @PatchMapping("/{eventId}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable @Positive long userId,
                                    @PathVariable @Positive long eventId,
                                    @PathVariable @Positive long commentId,
                                    @RequestBody @Valid NewCommentRequest commentRequest,
                                    HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventService.updateComment(userId, eventId, commentId, commentRequest);
    }

    @DeleteMapping("/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive long userId,
                              @PathVariable @Positive long eventId,
                              @PathVariable @Positive long commentId,
                              HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        eventService.deleteComment(userId, commentId);
    }
}
