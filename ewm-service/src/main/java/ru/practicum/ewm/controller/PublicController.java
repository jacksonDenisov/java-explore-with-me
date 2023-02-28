package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.model.category.CategoryDtoFull;
import ru.practicum.ewm.model.compilation.CompilationDtoFull;
import ru.practicum.ewm.model.event.EventDtoFull;
import ru.practicum.ewm.model.event.EventDtoShort;
import ru.practicum.ewm.model.event.EventSortOption;
import ru.practicum.ewm.service.category.CategoryService;
import ru.practicum.ewm.service.compilation.CompilationService;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.stats.StatsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class PublicController {

    private final CategoryService categoryService;

    private final EventService eventService;

    private final StatsService statsService;

    private final CompilationService compilationService;

    private static final String APP = "ewm-service";

    @GetMapping("/categories")
    protected List<CategoryDtoFull> findAllCategories(@RequestParam(required = false, defaultValue = "0")
                                                      @PositiveOrZero int from,
                                                      @RequestParam(required = false, defaultValue = "10")
                                                      @PositiveOrZero int size) {
        log.info("Получен запрос на получение категорий");
        Pageable pageable = PageRequest.of(from / size, size);
        return categoryService.findAll(pageable);
    }

    @GetMapping("/categories/{catId}")
    protected CategoryDtoFull findCategoryById(@PathVariable Long catId) {
        log.info("Получен запрос на получение категории с id {}", catId);
        return categoryService.findById(catId);
    }

    @GetMapping("/events")
    protected List<EventDtoShort> findAllPublicEvents(@RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Long> categories,
                                                      @RequestParam(required = false) Boolean paid,
                                                      @RequestParam(required = false) String rangeStart,
                                                      @RequestParam(required = false) String rangeEnd,
                                                      @RequestParam(required = false,
                                                              defaultValue = "false") Boolean onlyAvailable,
                                                      @RequestParam(required = false) EventSortOption sort,
                                                      @RequestParam(required = false, value = "from",
                                                              defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(required = false, value = "size",
                                                              defaultValue = "10") @PositiveOrZero int size,
                                                      HttpServletRequest httpServletRequest) {
        log.info("Получен запрос на получение списка событий");
        statsService.addHit(httpServletRequest, APP);
        Pageable pageable = PageRequest.of(from / size, size, getSortType(sort));
        return eventService.findAllPublicEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, sort, pageable);
    }

    @GetMapping("/events/{id}")
    protected EventDtoFull findPublicEventById(@PathVariable Long id,
                                               HttpServletRequest httpServletRequest) {
        log.info("Получен запрос на получение события с id {}", id);
        statsService.addHit(httpServletRequest, APP);
        return eventService.findPublicEventById(id);
    }

    @GetMapping("/compilations")
    protected List<CompilationDtoFull> getPublicCompilations(@RequestParam(required = false) Boolean pinned,
                                                             @RequestParam(required = false, value = "from",
                                                                     defaultValue = "0") @PositiveOrZero int from,
                                                             @RequestParam(required = false, value = "size",
                                                                     defaultValue = "10") @PositiveOrZero int size) {
        log.info("Получен запрос на получение подборок событий");
        Pageable pageable = PageRequest.of(from / size, size);
        return compilationService.getPublicCompilations(pinned, pageable);
    }

    @GetMapping("/compilations/{compId}")
    protected CompilationDtoFull getPublicCompilationById(@PathVariable Long compId) {
        log.info("Получен запрос на получение подборки событий с id {}", compId);
        return compilationService.getPublicCompilationById(compId);
    }

    private Sort getSortType(EventSortOption sort) {
        if (sort != null) {
            switch (sort) {
                case VIEWS:
                    return Sort.by("views").ascending();
                case EVENT_DATE:
                    return Sort.by("eventDate").ascending();
            }
        }
        return Sort.unsorted();
    }
}
