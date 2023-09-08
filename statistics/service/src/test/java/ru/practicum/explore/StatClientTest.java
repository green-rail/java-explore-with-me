package ru.practicum.explore;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.explore.client.StatClient;
import ru.practicum.explore.common.dto.EndpointHitDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class StatClientTest {

    @Autowired
    private StatClient statClient;

    @Test
    public void clientTest() {
        var dto1 = new EndpointHitDto();
        dto1.setApp("ewm-main-service");
        dto1.setUri("/events/1");
        dto1.setIp("192.163.0.1");
        dto1.setTimestamp(LocalDateTime.now().minusMinutes(10));
        var response1 = statClient.postHit(dto1);

        var dto2 = new EndpointHitDto();
        dto2.setApp("ewm-main-service");
        dto2.setUri("/events/2");
        dto2.setIp("192.163.0.1");
        dto2.setTimestamp(LocalDateTime.now());

        var dto3 = new EndpointHitDto();
        dto3.setApp("ewm-main-service");
        dto3.setUri("/events/1");
        dto3.setIp("192.163.0.1");
        dto3.setTimestamp(LocalDateTime.now());

        var response2 = statClient.postHit(dto2);
        statClient.postHit(dto3);
        var allStats = statClient.getStats(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), null, false);
        var statsForUri = statClient.getStats(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), new String[]{"/events/1"}, false);
        var statsForUriUnique = statClient.getStats(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1), new String[]{"/events/1"}, true);

        var cast1 = (ArrayList<Map>)statsForUriUnique.getBody();
        System.out.println(statsForUriUnique);

    }

}