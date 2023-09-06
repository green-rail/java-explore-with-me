package ru.practicum.explore.event.model;

import lombok.Value;

import javax.persistence.Embeddable;

@Value
@Embeddable
public class Location {
    float lat;
    float lon;
}
