package ru.practicum.ewm.model.estimations;

import lombok.Data;

@Data
public class EstimationDtoFull {

    private Long id;

    private Long userId;

    private Long event_id;

    private EstimationType estimationType;
}
