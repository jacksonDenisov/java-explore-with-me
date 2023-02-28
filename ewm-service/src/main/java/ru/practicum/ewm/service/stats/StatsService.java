package ru.practicum.ewm.service.stats;

import ru.practicum.ewm.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;

public interface StatsService {

    EndpointHitDto addHit(HttpServletRequest httpServletRequest, String app);
}
