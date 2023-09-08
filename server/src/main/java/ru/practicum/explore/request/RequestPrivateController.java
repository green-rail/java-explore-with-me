package ru.practicum.explore.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
@Validated
public class RequestPrivateController {

    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getUserRequests(@PathVariable @PositiveOrZero long userId,
                                                         HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addRequest(@PathVariable @PositiveOrZero long userId,
                                              @RequestParam @PositiveOrZero long eventId,
                                              HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        var addedRequest = requestService.addRequest(userId, eventId);
        log.debug("REQUEST ADDED " + addedRequest);
        return addedRequest;
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable @PositiveOrZero long userId,
                                                 @PathVariable @PositiveOrZero long requestId,
                                                 HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return requestService.cancelRequest(userId, requestId);
    }

}
