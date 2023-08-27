package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.common.dto.EndpointHitDto;
import ru.practicum.explore.common.dto.ViewStatsDto;
import ru.practicum.explore.model.EndpointDtoMapper;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;
import ru.practicum.explore.model.ViewStatsDtoMapper;
import ru.practicum.explore.storage.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsRepository repository;

    @Override
    @Transactional
    public EndpointHitDto addHit(EndpointHitDto hit) {
        EndpointHit entity = repository.save(EndpointDtoMapper.toEntity(hit));
        return EndpointDtoMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<ViewStats> stats;
        System.out.println("method hit");
        if (uris.length == 0 && !unique) {
            stats = repository.findViewStats(start, end);
        } else if (uris.length == 0) {
            stats = repository.findViewStatsUniqueIps(start, end);
        } else if (!unique) {
            stats = repository.findViewStatsByUris(start, end, uris);
        } else {
            stats = repository.findViewStatsByUrisUniqueIps(start, end, uris);
        }
        System.out.println("got response: " + stats);

        return ViewStatsDtoMapper.toDto(stats);
    }
}
