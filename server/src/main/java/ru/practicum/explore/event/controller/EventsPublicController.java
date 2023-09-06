package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.service.EventServiceAdmin;
import ru.practicum.explore.event.service.EventServicePublic;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventsPublicController {

    private final EventServicePublic eventServicePublic;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(defaultValue = "") String text,
                                         @RequestParam(defaultValue = "") List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) LocalDateTime rangeStart,
                                         @RequestParam(required = false) LocalDateTime rangeEnd,
                                         @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                         @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                         @RequestParam(defaultValue = "0")  @PositiveOrZero int from,
                                         @RequestParam(defaultValue = "10") @Positive int size,
                                         HttpServletRequest request) {


        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventServicePublic.getEventsPublic(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, from, size);

    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable @Positive long id, HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventServicePublic.getEventPublic(id);
    }
}
