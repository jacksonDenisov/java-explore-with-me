package ru.practicum.ewm.service.event;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.model.event.*;

import java.util.List;

public interface EventService {

    EventDtoFull create(EventDtoNew eventDtoNew, Long userId);

    EventDtoFull findEventByIdForInitiator(Long userId, Long eventId);

    List<EventDtoFull> findAllEventsForInitiator(Long userId, Pageable pageable);

    EventDtoFull updateEventByIdByInitiator(EventDtoUpdateByUser eventDtoUpdateByUser, Long userId, Long eventId);

    List<EventDtoFull> getEventsFullInfo(List<Long> users, List<EventState> states, List<Long> categories,
                                         String rangeStart, String rangeEnd, Pageable pageable);

    EventDtoFull updateEventByIdByAdmin(EventDtoUpdateByAdmin eventDtoUpdateByAdmin, Long eventId);

    List<EventDtoShort> findAllPublicEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                            String rangeEnd, Boolean onlyAvailable, EventSortOption sort,
                                            Pageable pageable);

    EventDtoFull findPublicEventById(Long id);
}
