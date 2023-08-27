package ru.practicum.explore.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.explore.common.Constants;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;

    @JsonFormat(pattern = Constants.HIT_DATETIME_FORMAT)
    private LocalDateTime timestamp;
}
