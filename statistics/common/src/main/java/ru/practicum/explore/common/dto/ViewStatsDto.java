package ru.practicum.explore.common.dto;

import lombok.Value;

@Value
public class ViewStatsDto {
    String app;
    String uri;
    long hits;
}
