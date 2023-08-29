package ru.practicum.explore.service;

import ru.practicum.explore.common.dto.EndpointHitDto;
import ru.practicum.explore.common.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {
    public EndpointHitDto addHit(EndpointHitDto hit);

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
