package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.explore.common.dto.EndpointHitDto;
import ru.practicum.explore.common.dto.ViewStatsDto;
import ru.practicum.explore.model.EndpointDtoMapper;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;
import ru.practicum.explore.model.ViewStatsDtoMapper;
import ru.practicum.explore.storage.StatisticsRepository;

import java.time.LocalDateTime;
import java.util.List;

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
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> stats;
        if (end.isBefore(start)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start must be before end");
        }

        if (unique) {
            if (uris.isEmpty()) {
                stats = repository.findViewStatsUniqueIps(start, end);
            } else {
                stats = repository.findViewStatsByUrisUniqueIps(start, end, uris);
            }
        } else {
            if (uris.isEmpty()) {
                stats = repository.findViewStats(start, end);
            } else {
                stats = repository.findViewStatsByUris(start, end, uris);
            }
        }
        return ViewStatsDtoMapper.toDto(stats);
    }
}
