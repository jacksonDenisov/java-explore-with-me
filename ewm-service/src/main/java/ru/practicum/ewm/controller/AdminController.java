package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.category.CategoryDtoFull;
import ru.practicum.ewm.model.category.CategoryDtoNew;
import ru.practicum.ewm.model.compilation.CompilationDtoFull;
import ru.practicum.ewm.model.compilation.CompilationDtoNew;
import ru.practicum.ewm.model.compilation.CompilationDtoUpdate;
import ru.practicum.ewm.model.event.EventDtoFull;
import ru.practicum.ewm.model.event.EventDtoUpdateByAdmin;
import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.model.user.UserDto;
import ru.practicum.ewm.model.user.UserDtoNew;
import ru.practicum.ewm.service.category.CategoryService;
import ru.practicum.ewm.service.compilation.CompilationService;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@Validated
public class AdminController {

    private final UserService userService;

    private final CategoryService categoryService;

    private final CompilationService compilationService;

    private final EventService eventService;


    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    protected UserDto createUser(@RequestBody @Valid UserDtoNew userDtoNew) {
        log.info("Получен запрос на добавление пользователя {}", userDtoNew);
        return userService.create(userDtoNew);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected void deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос на удаление пользователя с id {}", userId);
        userService.delete(userId);
    }

    @GetMapping("/users")
    protected List<UserDto> findUsers(@RequestParam List<Long> ids,
                                      @RequestParam(name = "from", required = false, defaultValue = "0")
                                      @PositiveOrZero int from,
                                      @RequestParam(name = "size", required = false, defaultValue = "10")
                                      @PositiveOrZero int size) {
        log.info("Получен запрос на поиск пользователей с id: {}. from = {}, size = {}", ids, from, size);
        Pageable pageable = PageRequest.of(from / size, size);
        return userService.findAllByIds(ids, pageable);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    protected CategoryDtoFull createCategory(@RequestBody @Valid CategoryDtoNew categoryDtoNew) {
        log.info("Получен запрос на создание категории {}", categoryDtoNew);
        return categoryService.create(categoryDtoNew);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected void deleteCategory(@PathVariable Long catId) {
        log.info("Получен запрос на удаление категории с id {}", catId);
        categoryService.delete(catId);
    }

    @PatchMapping("/categories/{catId}")
    protected CategoryDtoFull updateCategory(@RequestBody @Valid CategoryDtoNew categoryDtoNew,
                                             @PathVariable Long catId) {
        log.info("Получен запрос на обновление категории с id {}.", catId);
        return categoryService.update(categoryDtoNew, catId);
    }

    @GetMapping("/events")
    protected List<EventDtoFull> getEventsFullInfo(@RequestParam(required = false) List<Long> users,
                                                   @RequestParam(required = false) List<EventState> states,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false) String rangeStart,
                                                   @RequestParam(required = false) String rangeEnd,
                                                   @RequestParam(value = "from", defaultValue = "0")
                                                   @PositiveOrZero int from,
                                                   @RequestParam(value = "size", defaultValue = "10")
                                                   @PositiveOrZero int size) {
        log.info("Получен запрос на поиск событий");
        Pageable pageable = PageRequest.of(from / size, size);
        return eventService.getEventsFullInfo(users, states, categories, rangeStart, rangeEnd, pageable);
    }

    @PatchMapping("/events/{eventId}")
    protected EventDtoFull updateEvent(@RequestBody EventDtoUpdateByAdmin eventDtoUpdateByAdmin,
                                       @PathVariable Long eventId) {
        log.info("Получен запрос от администратора на обновление события с id {}", eventId);
        return eventService.updateEventByIdByAdmin(eventDtoUpdateByAdmin, eventId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    protected CompilationDtoFull createCompilation(@RequestBody @Valid CompilationDtoNew compilationDtoNew) {
        log.info("Получен запрос на создание подборки событий");
        return compilationService.createCompilation(compilationDtoNew);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected void deleteCompilationById(@PathVariable Long compId) {
        log.info("Получен запрос на удаление подборки событий с id {}", compId);
        compilationService.deleteCompilationById(compId);
    }

    @PatchMapping("/compilations/{compId}")
    protected CompilationDtoFull updateCompilationById(@RequestBody CompilationDtoUpdate compilationDtoUpdate,
                                                       @PathVariable Long compId) {
        log.info("Получен запрос на обновление подборки событий с id {}", compId);
        return compilationService.updateCompilationById(compilationDtoUpdate, compId);
    }
}
