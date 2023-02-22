package ru.practicum.ewm.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {

    String app;

    String uri;

    Long hits;
}
