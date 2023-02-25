package ru.practicum.ewm.service.event;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.model.event.EventDtoFull;
import ru.practicum.ewm.model.event.EventDtoNew;
import ru.practicum.ewm.model.event.EventDtoUpdate;

import java.util.List;

public interface EventService {

    EventDtoFull create(EventDtoNew eventDtoNew, Long userId);

    EventDtoFull findEventByIdForInitiator(Long userId, Long eventId);

    List<EventDtoFull> findAllEventsForInitiator(Long userId, Pageable pageable);

    EventDtoFull updateEventByIdByInitiator(EventDtoUpdate eventDtoUpdate, Long userId, Long eventId);
}
