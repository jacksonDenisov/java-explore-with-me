package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.event.EventDtoFull;
import ru.practicum.ewm.model.event.EventDtoNew;
import ru.practicum.ewm.model.event.EventDtoUpdateByUser;
import ru.practicum.ewm.model.participation_request.ParticipationRequestDto;
import ru.practicum.ewm.model.participation_request.ParticipationRequestDtoStatusUpdate;
import ru.practicum.ewm.model.participation_request.ParticipationRequestDtoUpdated;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.participation_request.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
@Slf4j
@Validated
public class PrivateController {

    private final EventService eventService;

    private final ParticipationRequestService participationRequestService;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    protected EventDtoFull createEvent(@PathVariable Long userId,
                                       @RequestBody @Valid EventDtoNew eventDtoNew) {
        log.info("Получен запрос на создание события {} от пользователя с id {}", eventDtoNew, userId);
        return eventService.create(eventDtoNew, userId);
    }

    @GetMapping("/events/{eventId}")
    protected EventDtoFull findEventByIdForInitiator(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.info("Получен запрос на получение события с id {} от пользователя с id {}", eventId, userId);
        return eventService.findEventByIdForInitiator(userId, eventId);
    }

    @GetMapping("/events")
    protected List<EventDtoFull> findAllEventsForInitiator(@PathVariable Long userId,
                                                           @RequestParam(required = false, defaultValue = "0")
                                                           @PositiveOrZero int from,
                                                           @RequestParam(required = false, defaultValue = "10")
                                                           @PositiveOrZero int size) {
        log.info("Получен запрос на получение списка всех событий для пользователя с id {}", userId);
        Pageable pageable = PageRequest.of(from / size, size);
        return eventService.findAllEventsForInitiator(userId, pageable);
    }

    @PatchMapping("/events/{eventId}")
    protected EventDtoFull updateEventByIdByInitiator(@PathVariable Long userId,
                                                      @PathVariable Long eventId,
                                                      @RequestBody @Valid EventDtoUpdateByUser eventDtoUpdateByUser) {
        log.info("Получен запрос на обновление события с id {} от пользователя с id {}", eventId, userId);
        return eventService.updateEventByIdByInitiator(eventDtoUpdateByUser, userId, eventId);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    protected ParticipationRequestDto createParticipationRequest(@PathVariable Long userId,
                                                                 @RequestParam Long eventId) {
        log.info("Получен запрос на участие в событии с id {} от пользователя с id {}", eventId, userId);
        return participationRequestService.createParticipationRequest(userId, eventId);
    }

    @GetMapping("/requests")
    protected List<ParticipationRequestDto> findParticipationRequestByUserId(@PathVariable Long userId) {
        log.info("Получен запрос на получение информации о заявках пользователя с id {}", userId);
        return participationRequestService.findParticipationRequestByRequesterId(userId);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    protected ParticipationRequestDto cancelParticipationRequest(@PathVariable Long userId,
                                                                 @PathVariable Long requestId) {
        log.info("Получен запрос на отмену заявки на участие в событии с id {} от пользователя с id {}", requestId, userId);
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }

    @GetMapping("/events/{eventId}/requests")
    protected List<ParticipationRequestDto> findParticipationRequestsForEventInitiator(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        log.info("Получен запрос на получение заявок на участие в событии с id {}", eventId);
        return participationRequestService.findParticipationRequestsForEventInitiator(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    protected ParticipationRequestDtoUpdated updateParticipationRequestStatusByEventInitiator(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody ParticipationRequestDtoStatusUpdate participationRequestDtoStatusUpdate) {
        log.info("Получен запрос на изменение статуса заявок на участие в событии с id {}", eventId);
        return participationRequestService.updateParticipationRequestStatusByEventInitiator(
                userId, eventId, participationRequestDtoStatusUpdate);
    }
}
