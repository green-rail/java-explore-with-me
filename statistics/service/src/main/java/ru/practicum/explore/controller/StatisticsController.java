package ru.practicum.explore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
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
    public EndpointHitDto saveHit(@RequestBody @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") EndpointHitDto hitDto,
                                  HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return statisticsService.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                       @RequestParam(required = false) String[] uris,
                                       @RequestParam(defaultValue = "false") boolean unique,
                                       HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return statisticsService.getStats(start, end,
                uris == null ? new String[]{} : uris,
                unique);
    }
}
