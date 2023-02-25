package ru.practicum.ewm.service.event;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.BusinessLogicConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.event.*;
import ru.practicum.ewm.model.location.Location;
import ru.practicum.ewm.model.location.LocationMapper;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.storage.category.CategoryRepository;
import ru.practicum.ewm.storage.event.EventRepository;
import ru.practicum.ewm.storage.location.LocationRepository;
import ru.practicum.ewm.storage.user.UserRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final LocationRepository locationRepository;

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public EventDtoFull create(EventDtoNew eventDtoNew, Long userId) {
        User initiator;
        Category category;
        Location location;
        try {
            initiator = userRepository.findById(userId).get();
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Не удалось найти пользователя",
                    "Пользователя с запрошенным id в системе не существует",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
        try {
            category = categoryRepository.findById(eventDtoNew.getCategory()).get();
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Не удалось найти категорию",
                    "Категория с запрошенным id в системе не существует",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
        if (eventDtoNew.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new BusinessLogicConflictException("Не удалось создать событие",
                    "Дата и время, на которые намечено событие не может быть раньше, чем через два часа от текущего момента",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        location = locationRepository.save(LocationMapper.toLocationNew(eventDtoNew.getLocation()));
        Event event = eventRepository.save(EventMapper.toNewEvent(eventDtoNew, initiator, location, category));
        return EventMapper.toEventDtoFull(event);
    }

    @Override
    @Transactional
    public EventDtoFull findEventByIdForInitiator(Long userId, Long eventId) {
        try {
            Event event = eventRepository.findById(eventId).get();
            if (event.getInitiator().getId() != userId) {
                throw new NotFoundException("Событие не доступно",
                        "У данного пользователя нет доступа к этому событию, так как он не является его инициатором",
                        new ArrayList<>(Collections.singletonList("NotFoundException")));
            }
            return EventMapper.toEventDtoFull(event);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Не удалось найти событие",
                    "События с запрошенным id в системе не существует",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public List<EventDtoFull> findAllEventsForInitiator(Long userId, Pageable pageable) {
        Page<Event> eventPage = eventRepository.findAllByInitiatorId(userId, pageable);
        return EventMapper.toEventDtoFull(eventPage.toList());
    }

    @Override
    @Transactional
    public EventDtoFull updateEventByIdByInitiator(EventDtoUpdate eventDtoUpdate, Long userId, Long eventId) {
        Event event;
        try {
            event = eventRepository.findById(eventId).get();
            if (event.getInitiator().getId() != userId) {
                throw new NotFoundException("Событие не доступно",
                        "У данного пользователя нет доступа к этому событию, так как он не является его инициатором",
                        new ArrayList<>(Collections.singletonList("NotFoundException")));
            }
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Не удалось найти событие",
                    "События с запрошенным id в системе не существует",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
        if (eventDtoUpdate.getAnnotation() != null) {
            event.setAnnotation(eventDtoUpdate.getAnnotation());
        }
        if (eventDtoUpdate.getCategory() != null) {
            try {
                Category category = categoryRepository.findById(eventDtoUpdate.getCategory()).get();
                event.setCategory(category);
            } catch (NoSuchElementException e) {
                throw new NotFoundException("Не удалось найти категорию",
                        "Категория с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList(e.getMessage())));
            }
        }
        if (eventDtoUpdate.getDescription() != null) {
            event.setDescription(eventDtoUpdate.getDescription());
        }
        if (eventDtoUpdate.getEventDate() != null) {
            if (eventDtoUpdate.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
                throw new BusinessLogicConflictException("Не удалось обновить событие",
                        "Дата и время, на которые намечено событие не может быть раньше, чем через два часа от текущего момента",
                        new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
            }
            event.setEventDate(eventDtoUpdate.getEventDate());
        }
        if (eventDtoUpdate.getLocation() != null) {
            Location location = locationRepository.findById(event.getLocation().getId()).get();
            location.setLon(eventDtoUpdate.getLocation().getLon());
            location.setLat(eventDtoUpdate.getLocation().getLat());
            locationRepository.save(location);
            event.setLocation(location);
        }
        if (eventDtoUpdate.getPaid() != null) {
            event.setPaid(eventDtoUpdate.getPaid());
        }
        if (eventDtoUpdate.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDtoUpdate.getParticipantLimit());
        }
        if (eventDtoUpdate.getRequestModeration() != null) {
            event.setRequestModeration(eventDtoUpdate.getRequestModeration());
        }
        if (eventDtoUpdate.getStateAction() != null) {
            if (event.getState() == EventState.CANCELED
                    && eventDtoUpdate.getStateAction() == EventStateAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            }
            if (event.getState() == EventState.PENDING
                    && eventDtoUpdate.getStateAction() == EventStateAction.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            } else {
                throw new BusinessLogicConflictException("Не удалось обновить статус события",
                        "Изменить можно только отмененные события или события в состоянии ожидания модерации",
                        new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
            }
        }
        if (eventDtoUpdate.getTitle() != null) {
            event.setTitle(eventDtoUpdate.getTitle());
        }
        Event updatedEvent = eventRepository.save(event);
        return EventMapper.toEventDtoFull(updatedEvent);
    }

    @Override
    @Transactional
    public List<EventDtoFull> getEventsFullInfo(List<Long> users, List<EventState> states, List<Long> categories,
                                                String rangeStart, String rangeEnd, Pageable pageable) {
        QEvent qEvent = QEvent.event;
        List<BooleanExpression> predicates = new ArrayList<>();
        if (users != null) {
            BooleanExpression byInitiatorId = qEvent.initiator.id.in(users);
            predicates.add(byInitiatorId);
        }
        if (states != null) {
            BooleanExpression byStates = qEvent.state.in(states);
            predicates.add(byStates);

        }
        if (categories != null) {
            BooleanExpression byCategories = qEvent.category.id.in(categories);
            predicates.add(byCategories);
        }
        if (rangeStart != null) {
            LocalDateTime start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
            BooleanExpression byRangeStart = qEvent.eventDate.after(start);
            predicates.add(byRangeStart);
        }
        if (rangeEnd != null) {
            LocalDateTime end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
            BooleanExpression byRangeEnd = qEvent.eventDate.before(end);
            predicates.add(byRangeEnd);
        }
        BooleanExpression finalPredicate = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            finalPredicate = finalPredicate.and(predicate);
        }
        Page<Event> eventPage = eventRepository.findAll(finalPredicate, pageable);
        return EventMapper.toEventDtoFull(eventPage.toList());
    }
}
