package ru.practicum.ewm.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsWebClient {

    private final WebClient webClient;

    private static final String POST_HIT_URL = "/hit";

    private static final String GET_STATS_URL = "/stats";


    public EndpointHitDto postHit(EndpointHitDto endpointHitDto) {
        return webClient
                .post()
                .uri(POST_HIT_URL)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(endpointHitDto), EndpointHitDto.class)
                .retrieve()
                .bodyToMono(EndpointHitDto.class)
                .doOnError(error -> log.error("Ошибка при отправке запроса postHit: {}.", error.getMessage()))
                .block();
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris) {
        return Collections.singletonList(webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(GET_STATS_URL)
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .build())
                .retrieve()
                .bodyToMono(ViewStatsDto.class)
                .doOnError(error -> log.error("Ошибка при отправке запроса getStats: {}.", error.getMessage()))
                .block());
    }

    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        return Collections.singletonList(webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(GET_STATS_URL)
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .bodyToMono(ViewStatsDto.class)
                .doOnError(error -> log.error("Ошибка при отправке запроса getStats(unique): {}.", error.getMessage()))
                .block());
    }
}
