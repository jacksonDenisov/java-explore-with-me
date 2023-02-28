package ru.practicum.ewm.model.compilation;

import lombok.Data;
import ru.practicum.ewm.model.event.EventDtoShort;

import java.util.List;

@Data
public class CompilationDtoFull {

    private Long id;

    private List<EventDtoShort> events;

    private Boolean pinned;

    private String title;
}
