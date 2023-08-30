package ru.practicum.explore.model;

import lombok.Value;

@Value
public class ViewStats {
    String app;
    String uri;
    long hits;
}
