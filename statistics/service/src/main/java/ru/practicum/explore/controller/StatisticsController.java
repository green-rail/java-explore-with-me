package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.common.dto.EndpointHitDto;
import ru.practicum.explore.common.dto.ViewStatsDto;
import ru.practicum.explore.service.StatisticsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto saveHit(@RequestBody EndpointHitDto hitDto,
                                  HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        var result = statisticsService.addHit(hitDto);
        log.debug("SAVED STAT HIT: " + result);

        return result;
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = Constants.HIT_DATETIME_FORMAT) LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = Constants.HIT_DATETIME_FORMAT) LocalDateTime end,
                                       @RequestParam(defaultValue = "") List<String> uris,
                                       @RequestParam(defaultValue = "false") boolean unique,
                                       HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        var stats = statisticsService.getStats(start, end, uris, unique);
        log.debug("RETURNING STATISTICS: " + stats);
        return stats;
    }
}
