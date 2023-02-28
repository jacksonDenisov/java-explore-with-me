package ru.practicum.ewm.service.compilation;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.model.compilation.CompilationDtoFull;
import ru.practicum.ewm.model.compilation.CompilationDtoNew;
import ru.practicum.ewm.model.compilation.CompilationDtoUpdate;

import java.util.List;

public interface CompilationService {

    CompilationDtoFull createCompilation(CompilationDtoNew compilationDtoNew);

    void deleteCompilationById(Long compId);

    CompilationDtoFull updateCompilationById(CompilationDtoUpdate compilationDtoUpdate, Long compId);

    List<CompilationDtoFull> getPublicCompilations(Boolean pinned, Pageable pageable);

    CompilationDtoFull getPublicCompilationById(Long compId);
}
