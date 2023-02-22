package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EndpointHitDto;
import ru.practicum.ewm.dto.ViewStatsDto;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.EndpointHitMapper;
import ru.practicum.ewm.model.ViewStatsMapper;
import ru.practicum.ewm.storage.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statsRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        return EndpointHitMapper.toEndpointHitDto(endpointHit);
    }

    @Override
    @Transactional
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        if (unique) {
            return ViewStatsMapper.toViewStatsDtos(
                    statsRepository.findViewStatsByUniqIp(convertData(start), convertData(end), uris));
        }
        return ViewStatsMapper.toViewStatsDtos(
                statsRepository.findViewStats(convertData(start), convertData(end), uris));
    }

    private LocalDateTime convertData(String date) {
        return LocalDateTime.parse(URLDecoder.decode(date, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
    }
}
