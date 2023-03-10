package ru.practicum.ewm.model.compilation;

import lombok.Data;

import java.util.List;

@Data
public class CompilationDtoUpdate {

    private List<Long> events;

    private Boolean pinned;

    private String title;
}
