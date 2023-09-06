package ru.practicum.explore.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.UpdateEventAdminRequest;
import ru.practicum.explore.event.service.EventServiceAdmin;

import javax.servlet.http.HttpServletRequest;
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
    public List<EventFullDto> getEvents(@RequestParam(defaultValue = "") List<Long> users,
                                        @RequestParam(defaultValue = "") List<String> states,
                                        @RequestParam(defaultValue = "") List<Long> categories,
                                        @RequestParam LocalDateTime rangeStart,
                                        @RequestParam LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0")  @PositiveOrZero int from,
                                        @RequestParam(defaultValue = "10") @Positive int size,
                                        HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventServiceAdmin.getEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId,
                                    UpdateEventAdminRequest eventUpdateDto,
                                    HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return eventServiceAdmin.updateEventAdmin(eventId, eventUpdateDto);
    }


}
