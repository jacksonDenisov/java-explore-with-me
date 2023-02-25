package ru.practicum.ewm.model.location;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LocationDtoNew {

    @NotNull
    private Float lat;

    @NotNull
    private Float lon;
}
