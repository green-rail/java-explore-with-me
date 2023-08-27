package ru.practicum.explore.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explore.common.Constants;
import ru.practicum.explore.common.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.net.UrlEscapers.urlPathSegmentEscaper;


@Service
public final class StatClient {
    private final RestTemplate rest;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.HIT_DATETIME_FORMAT);

    @Autowired
    public StatClient(@Value("${stat-service.url}") String statServiceUrl, RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(statServiceUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<Object> postHit(EndpointHitDto dto) {
        return makeAndSendRequest(HttpMethod.POST, "/hit", null, dto);
    }

    public ResponseEntity<Object> getStats(LocalDateTime start, LocalDateTime end, @Nullable String[] uris, boolean unique) {

        if (!isValidStartEnd(start, end)) {
            throw new IllegalArgumentException("Неверные временные рамки");
        }

        var parameters = new HashMap<String, Object>();
        parameters.put("start", formatter.format(start));
        parameters.put("end", formatter.format(end));
        String path = "/stats?start={start}&end={end}&unique={unique}";
        if (uris != null && uris.length > 0) {
            String[] escapedUris = Arrays.stream(uris)
                    .map(urlPathSegmentEscaper()::escape)
                    .toArray(String[]::new);
            path += "&uris={uris}";
            parameters.put("uris", escapedUris);
        }
        parameters.put("unique", unique);
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    private static boolean isValidStartEnd(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(LocalDateTime.now())) {
            return false;
        }
        return start.isBefore(end);
    }


    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<Object> response;
        try {
            if (parameters != null) {
                response = rest.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                response = rest.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return prepareResponse(response);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }

    private static ResponseEntity<Object> prepareResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

}
