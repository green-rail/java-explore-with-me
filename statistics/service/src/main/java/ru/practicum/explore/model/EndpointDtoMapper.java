package ru.practicum.explore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.EndpointHitDto;

import javax.persistence.Column;
import java.time.LocalDateTime;

@UtilityClass
public class EndpointDtoMapper {
    public EndpointHitDto toDto(EndpointHit hit) {
        return new EndpointHitDto(hit.getId(), hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
    }

    public EndpointHit toEntity(EndpointHitDto dto) {
        var hit = new EndpointHit();
        hit.setApp(dto.getApp());
        hit.setUri(dto.getUri());
        hit.setIp(dto.getIp());
        hit.setTimestamp(dto.getTimestamp());
        return hit;
    }

}
