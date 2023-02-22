package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
@Validated
@Slf4j
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping(value = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    protected EndpointHitDto addHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        log.info("Получен запрос к эндпоинту {}, сервис: {}", endpointHitDto.getUri(), endpointHitDto.getApp());
        return statsService.addHit(endpointHitDto);
    }

    @GetMapping(value = "/stats")
    protected List<ViewStatsDto> getStats(@RequestParam String start, @RequestParam String end,
                                          @RequestParam List<String> uris,
                                          @RequestParam(required = false, defaultValue = "false") Boolean unique) {
        log.info("Получен запрос на получение статистики за период {} - {}. unique: {}. uris: {}", start, end, unique, uris);
        return statsService.getStats(start, end, uris, unique);
    }
}
