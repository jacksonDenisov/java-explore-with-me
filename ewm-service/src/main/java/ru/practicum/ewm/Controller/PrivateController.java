package ru.practicum.ewm.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.event.EventDtoFull;
import ru.practicum.ewm.model.event.EventDtoNew;
import ru.practicum.ewm.model.event.EventDtoUpdate;
import ru.practicum.ewm.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    protected EventDtoFull createEvent(@PathVariable @Positive Long userId,
                                       @RequestBody @Valid EventDtoNew eventDtoNew) {
        log.info("Получен запрос на создание события {} от пользователя с id {}", eventDtoNew, userId);
        return eventService.create(eventDtoNew, userId);
    }

    @GetMapping("/{eventId}")
    protected EventDtoFull findEventByIdForInitiator(@PathVariable @Positive Long userId,
                                                     @PathVariable @Positive Long eventId) {
        log.info("Получен запрос на получение события с id {} от пользователя с id {}", eventId, userId);
        return eventService.findEventByIdForInitiator(userId, eventId);
    }

    @GetMapping
    protected List<EventDtoFull> findAllEventsForInitiator(@PathVariable @Positive Long userId,
                                                           @RequestParam(required = false, defaultValue = "0")
                                                           @PositiveOrZero int from,
                                                           @RequestParam(required = false, defaultValue = "10")
                                                           @PositiveOrZero int size) {
        log.info("Получен запрос на получение списка всех событий для пользователя с id {}", userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return eventService.findAllEventsForInitiator(userId, pageable);
    }

    @PatchMapping("/{eventId}")
    protected EventDtoFull updateEventByIdByInitiator(@PathVariable @Positive Long userId,
                                                      @PathVariable @Positive Long eventId,
                                                      @RequestBody @Valid EventDtoUpdate eventDtoUpdate) {
        log.info("Получен запрос на обновление события с id {} от пользователя с id {}", eventId, userId);
        return eventService.updateEventByIdByInitiator(eventDtoUpdate, userId, eventId);
    }
}
