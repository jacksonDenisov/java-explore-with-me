package ru.practicum.ewm.service.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

import static ru.practicum.ewm.model.event.EventState.PUBLISHED;

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
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти пользователя",
                        "Пользователя с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        Category category = categoryRepository.findById(eventDtoNew.getCategory())
                .orElseThrow(() -> new NotFoundException("Не удалось найти категорию",
                        "Категория с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (eventDtoNew.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new BusinessLogicConflictException("Не удалось создать событие",
                    "Дата и время, на которые намечено событие не может быть раньше, чем через два часа от текущего момента",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        Location location = locationRepository.save(LocationMapper.toLocationNew(eventDtoNew.getLocation()));
        Event event = eventRepository.save(EventMapper.toNewEvent(eventDtoNew, initiator, location, category));
        return EventMapper.toEventDtoFull(event);
    }

    @Override
    @Transactional
    public EventDtoFull findEventByIdForInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти событие",
                        "События с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Событие не доступно",
                    "У данного пользователя нет доступа к этому событию, так как он не является его инициатором",
                    new ArrayList<>(Collections.singletonList("NotFoundException")));
        }
        return EventMapper.toEventDtoFull(event);


    }

    @Override
    @Transactional
    public List<EventDtoFull> findAllEventsForInitiator(Long userId, Pageable pageable) {
        return EventMapper.toEventDtoFull(
                eventRepository.findAllByInitiatorId(userId, pageable).toList());
    }

    @Override
    @Transactional
    public EventDtoFull updateEventByIdByInitiator(EventDtoUpdateByUser eventDtoUpdateByUser, Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти событие",
                        "События с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("Событие не доступно",
                    "У данного пользователя нет доступа к этому событию, так как он не является его инициатором",
                    new ArrayList<>(Collections.singletonList("NotFoundException")));
        }
        if (event.getState().equals(PUBLISHED)) {
            throw new BusinessLogicConflictException("Не удалось обновить событие",
                    "Нельзя изменять опубликованное событие",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        if (eventDtoUpdateByUser.getAnnotation() != null) {
            event.setAnnotation(eventDtoUpdateByUser.getAnnotation());
        }
        if (eventDtoUpdateByUser.getCategory() != null) {
            Category category = categoryRepository.findById(eventDtoUpdateByUser.getCategory())
                    .orElseThrow(() -> new NotFoundException("Не удалось найти категорию",
                            "Категория с запрошенным id в системе не существует",
                            new ArrayList<>(Collections.singletonList("NotFoundException"))));
            event.setCategory(category);
        }
        if (eventDtoUpdateByUser.getDescription() != null) {
            event.setDescription(eventDtoUpdateByUser.getDescription());
        }
        if (eventDtoUpdateByUser.getEventDate() != null) {
            if (eventDtoUpdateByUser.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
                throw new BusinessLogicConflictException("Не удалось обновить событие",
                        "Дата и время, на которые намечено событие не может быть раньше, чем через два часа от текущего момента",
                        new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
            }
            event.setEventDate(eventDtoUpdateByUser.getEventDate());
        }
        if (eventDtoUpdateByUser.getLocation() != null) {
            Location location = locationRepository.findById(event.getLocation().getId()).get();
            location.setLon(eventDtoUpdateByUser.getLocation().getLon());
            location.setLat(eventDtoUpdateByUser.getLocation().getLat());
            locationRepository.save(location);
            event.setLocation(location);
        }
        if (eventDtoUpdateByUser.getPaid() != null) {
            event.setPaid(eventDtoUpdateByUser.getPaid());
        }
        if (eventDtoUpdateByUser.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDtoUpdateByUser.getParticipantLimit());
        }
        if (eventDtoUpdateByUser.getRequestModeration() != null) {
            event.setRequestModeration(eventDtoUpdateByUser.getRequestModeration());
        }
        if (eventDtoUpdateByUser.getStateAction() != null) {
            if (event.getState() == EventState.CANCELED
                    && eventDtoUpdateByUser.getStateAction() == EventStateAction.SEND_TO_REVIEW) {
                event.setState(EventState.PENDING);
            } else if (event.getState() == EventState.PENDING
                    && eventDtoUpdateByUser.getStateAction() == EventStateAction.CANCEL_REVIEW) {
                event.setState(EventState.CANCELED);
            } else {
                throw new BusinessLogicConflictException("Не удалось обновить статус события",
                        "Изменить можно только отмененные события или события в состоянии ожидания модерации",
                        new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
            }
        }
        if (eventDtoUpdateByUser.getTitle() != null) {
            event.setTitle(eventDtoUpdateByUser.getTitle());
        }
        return EventMapper.toEventDtoFull(eventRepository.save(event));
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
        return EventMapper.toEventDtoFull(
                eventRepository.findAll(finalPredicate, pageable).toList());
    }

    @Override
    @Transactional
    public EventDtoFull updateEventByIdByAdmin(EventDtoUpdateByAdmin eventDtoUpdateByAdmin, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти событие",
                        "События с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (eventDtoUpdateByAdmin.getAnnotation() != null) {
            event.setAnnotation(eventDtoUpdateByAdmin.getAnnotation());
        }
        if (eventDtoUpdateByAdmin.getCategory() != null) {
            Category category = categoryRepository.findById(eventDtoUpdateByAdmin.getCategory())
                    .orElseThrow(() -> new NotFoundException("Не удалось найти категорию",
                            "Категория с запрошенным id в системе не существует",
                            new ArrayList<>(Collections.singletonList("NotFoundException"))));
            event.setCategory(category);
        }
        if (eventDtoUpdateByAdmin.getDescription() != null) {
            event.setDescription(eventDtoUpdateByAdmin.getDescription());
        }
        if (eventDtoUpdateByAdmin.getEventDate() != null && event.getCreatedOn() != null) {
            if (eventDtoUpdateByAdmin.getEventDate().isBefore(event.getCreatedOn().plusHours(1L))) {
                throw new BusinessLogicConflictException("Не удалось обновить событие",
                        "Дата начала изменяемого события должна быть не ранее, чем за час от даты публикации",
                        new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
            }
            event.setEventDate(eventDtoUpdateByAdmin.getEventDate());
        }
        if (eventDtoUpdateByAdmin.getLocation() != null) {
            Location location = locationRepository.findById(event.getLocation().getId()).get();
            location.setLon(eventDtoUpdateByAdmin.getLocation().getLon());
            location.setLat(eventDtoUpdateByAdmin.getLocation().getLat());
            locationRepository.save(location);
            event.setLocation(location);
        }
        if (eventDtoUpdateByAdmin.getPaid() != null) {
            event.setPaid(eventDtoUpdateByAdmin.getPaid());
        }
        if (eventDtoUpdateByAdmin.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDtoUpdateByAdmin.getParticipantLimit());
        }
        if (eventDtoUpdateByAdmin.getRequestModeration() != null) {
            event.setRequestModeration(eventDtoUpdateByAdmin.getRequestModeration());
        }
        if (eventDtoUpdateByAdmin.getStateAction() != null) {
            if (event.getState() == EventState.PENDING
                    && eventDtoUpdateByAdmin.getStateAction() == EventStateAction.PUBLISH_EVENT) {
                event.setState(PUBLISHED);
            } else if (event.getState() == EventState.PENDING
                    && eventDtoUpdateByAdmin.getStateAction() == EventStateAction.REJECT_EVENT) {
                event.setState(EventState.CANCELED);
            } else {
                throw new BusinessLogicConflictException("Не удалось обновить статус события",
                        "Событие можно публиковать, только если оно в состоянии ожидания публикации. " +
                                "Событие можно отклонить, только если оно еще не опубликовано",
                        new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
            }
        }
        if (eventDtoUpdateByAdmin.getTitle() != null) {
            event.setTitle(eventDtoUpdateByAdmin.getTitle());
        }
        return EventMapper.toEventDtoFull(eventRepository.save(event));
    }

    @Override
    @Transactional
    public List<EventDtoShort> findAllPublicEvents(String text, List<Long> categories, Boolean paid,
                                                   String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                                   EventSortOption sort, Pageable pageable) {
        QEvent qEvent = QEvent.event;
        List<BooleanExpression> predicates = new ArrayList<>();

        BooleanExpression byPublishedState = qEvent.state.eq(PUBLISHED);
        predicates.add(byPublishedState);

        if (text != null) {
            BooleanExpression byTextInAnnotationOrDescription = qEvent.annotation.containsIgnoreCase(text)
                    .or(qEvent.description.containsIgnoreCase(text));
            predicates.add(byTextInAnnotationOrDescription);
        }
        if (categories != null) {
            BooleanExpression byCategories = qEvent.category.id.in(categories);
            predicates.add(byCategories);
        }
        if (paid != null) {
            BooleanExpression byPaid = qEvent.paid.eq(paid);
            predicates.add(byPaid);
        }
        if (rangeStart != null && rangeEnd != null) {
            LocalDateTime start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
            BooleanExpression byRangeStart = qEvent.eventDate.after(start);
            predicates.add(byRangeStart);

            LocalDateTime end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8),
                    DateTimeFormatter.ofPattern(("yyyy-MM-dd HH:mm:ss")));
            BooleanExpression byRangeEnd = qEvent.eventDate.before(end);
            predicates.add(byRangeEnd);
        } else {
            BooleanExpression defaultTimePredicate = qEvent.eventDate.after(LocalDateTime.now());
            predicates.add(defaultTimePredicate);

        }
        if (onlyAvailable) {
            BooleanExpression byOnlyAvailable = qEvent.participantLimit.gt(qEvent.confirmedRequests);
            predicates.add(byOnlyAvailable);
        }
        BooleanExpression finalPredicate = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            finalPredicate = finalPredicate.and(predicate);
        }
        return EventMapper.toEventDtoShort(
                eventRepository.findAll(finalPredicate, pageable).toList());
    }

    @Override
    @Transactional
    public EventDtoFull findPublicEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Не удалось найти событие",
                        "События с запрошенным id в системе не существует или оно недоступно",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (event.getState() != PUBLISHED) {
            throw new NotFoundException("Не удалось найти событие",
                    "События с запрошенным id недоступно",
                    new ArrayList<>(Collections.singletonList("NotFoundException")));
        }
        event.setViews(event.getViews() + 1);
        return EventMapper.toEventDtoFull(
                eventRepository.save(event));
    }
}
