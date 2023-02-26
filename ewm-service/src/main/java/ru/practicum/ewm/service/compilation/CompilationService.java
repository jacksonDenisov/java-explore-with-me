package ru.practicum.ewm.service.compilation;

import ru.practicum.ewm.model.compilation.CompilationDtoFull;
import ru.practicum.ewm.model.compilation.CompilationDtoNew;

public interface CompilationService {

    CompilationDtoFull createCompilation(CompilationDtoNew compilationDtoNew);
}
