package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequest;
import ru.practicum.explore.event.service.EventServiceAdmin;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventsAdminController {

    private final EventServiceAdmin eventServiceAdmin;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Long> users,
                                        @RequestParam(required = false) List<String> states,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT) LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = Constants.DEFAULT_DATETIME_FORMAT) LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0")  @PositiveOrZero int from,
                                        @RequestParam(defaultValue = "10") @Positive int size,
                                        HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventServiceAdmin.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequest eventUpdateDto,
                                    HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        var patched = eventServiceAdmin.updateEventAdmin(eventId, eventUpdateDto);
        log.debug("EVENT PATCHED: {}", patched);
        return patched;
    }


}
