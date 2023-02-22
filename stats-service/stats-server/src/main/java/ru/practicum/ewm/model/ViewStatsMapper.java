package ru.practicum.ewm.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.dto.ViewStatsDto;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewStatsMapper {

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        ViewStatsDto viewStatsDto = new ViewStatsDto();
        viewStatsDto.setApp(viewStats.getApp());
        viewStatsDto.setUri(viewStats.getUri());
        viewStatsDto.setHits(viewStats.getHits());
        return viewStatsDto;
    }

    public static List<ViewStatsDto> toViewStatsDtos(List<ViewStats> viewStatsList) {
        List<ViewStatsDto> viewStatsDtos = new ArrayList<>();
        for (ViewStats viewStats : viewStatsList) {
            viewStatsDtos.add(toViewStatsDto(viewStats));
        }
        return viewStatsDtos;
    }
}
