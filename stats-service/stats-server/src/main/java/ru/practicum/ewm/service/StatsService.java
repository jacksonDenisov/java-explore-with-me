package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.util.List;

public interface StatsService {

    EndpointHitDto addHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique);
}
