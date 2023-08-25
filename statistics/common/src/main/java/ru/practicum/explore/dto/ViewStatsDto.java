package ru.practicum.explore.dto;

import lombok.Value;

@Value
public class ViewStatsDto {
    String app;
    String uri;
    long hits;
}
