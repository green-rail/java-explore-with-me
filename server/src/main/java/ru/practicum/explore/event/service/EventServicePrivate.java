package ru.practicum.explore.event.service;

import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.dto.NewCommentRequest;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.request.dto.ParticipationRequestDto;

import java.util.List;

public interface EventServicePrivate {

    List<EventShortDto> getUserEvents(long userId, int from, int size);

    EventFullDto addNewEvent(long userId, NewEventDto eventDto);

    EventFullDto getUserEvent(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest eventDto);

    List<ParticipationRequestDto> getUserEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult updateUserEventRequestStatus(long userId, long eventId,
                                                                      EventRequestStatusUpdateRequest updateRequest);

    CommentDto postComment(long userId, long eventId, NewCommentRequest commentRequest);

    CommentDto updateComment(long userId, long eventId, long commentId, NewCommentRequest commentRequest);

    void deleteComment(long userId, long commentId);
}
