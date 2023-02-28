package ru.practicum.ewm.service.compilation;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.DataBaseConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.model.compilation.*;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.storage.compilation.CompilationRepository;
import ru.practicum.ewm.storage.event.EventRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDtoFull createCompilation(CompilationDtoNew compilationDtoNew) {
        List<Event> events = new ArrayList<>();
        if (compilationDtoNew.getEvents() != null && !compilationDtoNew.getEvents().isEmpty()) {
            events = eventRepository.findAllByIdIn(compilationDtoNew.getEvents());
        }
        if (compilationDtoNew.getPinned() == null) {
            compilationDtoNew.setPinned(false);
        }
        try {
            return CompilationMapper.toCompilationDtoFull(compilationRepository.save(
                    CompilationMapper.toCompilationNew(compilationDtoNew, events)));
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseConflictException("Не удалось добавить новую подборку событий",
                    "Нарушение целостности данных",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public void deleteCompilationById(Long compId) {
        try {
            compilationRepository.deleteById(compId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Не удалось удалить подборку событий",
                    "Такой подборки в системе не существует",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public CompilationDtoFull updateCompilationById(CompilationDtoUpdate compilationDtoUpdate, Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не удалось обновить подборку событий",
                        "Вероятно, указан несуществующий id",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (compilationDtoUpdate.getTitle() != null) {
            if (compilation.getTitle().equals(compilationDtoUpdate.getTitle())) {
                throw new DataBaseConflictException("Не удалось обновить подборку событий",
                        "Нарушение целостности данных - указанный заголовок уже задействован",
                        new ArrayList<>(Collections.singletonList("DataBaseConflictException")));
            }
            compilation.setTitle(compilationDtoUpdate.getTitle());
        }
        if (compilationDtoUpdate.getPinned() != null) {
            compilation.setPinned(compilationDtoUpdate.getPinned());
        }
        if (compilationDtoUpdate.getPinned() != null && !compilationDtoUpdate.getEvents().isEmpty()) {
            compilation.setEvents(eventRepository.findAllByIdIn(compilationDtoUpdate.getEvents()));
        }
        return CompilationMapper.toCompilationDtoFull(compilationRepository.save(compilation));
    }

    @Override
    @Transactional
    public List<CompilationDtoFull> getPublicCompilations(Boolean pinned, Pageable pageable) {
        QCompilation qCompilation = QCompilation.compilation;
        Page<Compilation> compilationPage;
        if (pinned != null) {
            BooleanExpression byPinned;
            if (pinned) {
                byPinned = qCompilation.pinned.isTrue();
            } else {
                byPinned = qCompilation.pinned.isFalse();
            }
            compilationPage = compilationRepository.findAll(byPinned, pageable);
        } else {
            compilationPage = compilationRepository.findAll(pageable);
        }
        return CompilationMapper.toCompilationDtoFull(compilationPage.toList());
    }

    @Override
    @Transactional
    public CompilationDtoFull getPublicCompilationById(Long compId) {
        return CompilationMapper.toCompilationDtoFull(compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти подборку событий",
                        "Подборка с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException")))));
    }
}
