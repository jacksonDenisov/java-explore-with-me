package ru.practicum.ewm.service.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.StatsWebClient;
import ru.practicum.ewm.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsWebClient statsWebClient;

    public EndpointHitDto addHit(HttpServletRequest httpServletRequest, String app) {
        EndpointHitDto endpointHitDto = new EndpointHitDto();
        endpointHitDto.setApp(app);
        endpointHitDto.setUri(httpServletRequest.getRequestURI());
        endpointHitDto.setIp(httpServletRequest.getRemoteAddr());
        endpointHitDto.setTimestamp(LocalDateTime.now());
        return statsWebClient.postHit(endpointHitDto);
    }
}
