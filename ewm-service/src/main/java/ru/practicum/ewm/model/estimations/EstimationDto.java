package ru.practicum.ewm.model.estimations;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EstimationDto {

    @NotNull
    private EstimationType estimationType;
}
