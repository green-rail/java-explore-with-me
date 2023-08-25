package ru.practicum.explore.model;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.ViewStatsDto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ViewStatsDtoMapper {


    public static ViewStatsDto toDto(ViewStats stat) {
        return new ViewStatsDto(stat.getApp(), stat.getUri(), stat.getHits());
    }


    public static List<ViewStatsDto> toDto(Collection<ViewStats> stats) {
        return stats.stream().map(ViewStatsDtoMapper::toDto).collect(Collectors.toUnmodifiableList());
    }
}
