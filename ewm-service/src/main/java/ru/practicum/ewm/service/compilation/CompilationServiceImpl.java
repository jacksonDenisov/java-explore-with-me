package ru.practicum.ewm.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.model.compilation.CompilationDtoFull;
import ru.practicum.ewm.model.compilation.CompilationDtoNew;
import ru.practicum.ewm.model.compilation.CompilationMapper;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.storage.compilation.CompilationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    @Override
    @Transactional
    public CompilationDtoFull createCompilation(CompilationDtoNew compilationDtoNew) {
        List<Event> events = new ArrayList<>();
        if (compilationDtoNew.getEvents()!= null && !compilationDtoNew.getEvents().isEmpty()){

        }

    }
}
