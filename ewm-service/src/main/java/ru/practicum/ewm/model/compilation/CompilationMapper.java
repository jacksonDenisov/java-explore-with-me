package ru.practicum.ewm.model.compilation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventMapper;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {

    public static Compilation toCompilationNew(CompilationDtoNew compilationDtoNew, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setPinned(compilationDtoNew.getPinned());
        compilation.setTitle(compilationDtoNew.getTitle());
        return compilation;
    }

    public static CompilationDtoFull toCompilationDtoFull(Compilation compilation) {
        CompilationDtoFull compilationDtoFull = new CompilationDtoFull();
        compilationDtoFull.setId(compilation.getId());
        compilationDtoFull.setEvents(EventMapper.toEventDtoShort(compilation.getEvents()));
        compilationDtoFull.setPinned(compilation.getPinned());
        compilationDtoFull.setTitle(compilation.getTitle());
        return compilationDtoFull;
    }
}
