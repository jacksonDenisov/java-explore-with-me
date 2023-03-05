package ru.practicum.ewm.model.estimations;

import lombok.Data;

@Data
public class EstimationDtoFull {

    private Long id;

    private Long userId;

    private Long eventId;

    private EstimationType estimationType;
}
