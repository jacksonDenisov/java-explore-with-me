package ru.practicum.ewm.model.compilation;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CompilationDtoNew {

    private List<Long> events;

    private Boolean pinned;

    @NotBlank
    private String title;
}
