package ru.practicum.ewm.model.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.location.Location;
import ru.practicum.ewm.model.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event toNewEvent(EventDtoNew eventDtoNew, User initiator, Location location, Category category) {
        Event event = new Event();
        event.setAnnotation(eventDtoNew.getAnnotation());
        event.setCategory(category);
        event.setDescription(eventDtoNew.getDescription());
        event.setEventDate(eventDtoNew.getEventDate());
        event.setLocation(location);
        if (eventDtoNew.getPaid() != null) {
            event.setPaid(eventDtoNew.getPaid());
        } else {
            event.setPaid(false);
        }
        if (eventDtoNew.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDtoNew.getParticipantLimit());
        } else {
            event.setParticipantLimit(0);
        }
        if (eventDtoNew.getRequestModeration() != null) {
            event.setRequestModeration(eventDtoNew.getRequestModeration());
        } else {
            event.setRequestModeration(true);
        }
        event.setTitle(eventDtoNew.getTitle());
        event.setConfirmedRequests(0L);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(initiator);
        event.setState(EventState.PENDING);
        event.setViews(0L);
        return event;
    }

    public static EventDtoFull toEventDtoFull(Event event) {
        EventDtoFull eventDtoFull = new EventDtoFull();
        eventDtoFull.setId(event.getId());
        eventDtoFull.setAnnotation(event.getAnnotation());
        eventDtoFull.setCategory(new EventDtoFull.Category(
                event.getCategory().getId(),
                event.getCategory().getName()));
        eventDtoFull.setConfirmedRequests(event.getConfirmedRequests());
        eventDtoFull.setCreatedOn(event.getCreatedOn());
        eventDtoFull.setDescription(event.getDescription());
        eventDtoFull.setEventDate(event.getEventDate());
        eventDtoFull.setInitiator(new EventDtoFull.User(
                event.getInitiator().getId(),
                event.getInitiator().getName()));
        eventDtoFull.setLocation(new EventDtoFull.Location(
                event.getLocation().getLat(),
                event.getLocation().getLon()));
        eventDtoFull.setPaid(event.getPaid());
        eventDtoFull.setParticipantLimit(event.getParticipantLimit());
        if (event.getPublishedOn() != null) {
            eventDtoFull.setPublishedOn(event.getPublishedOn());
        }
        eventDtoFull.setRequestModeration(event.getRequestModeration());
        eventDtoFull.setState(event.getState());
        eventDtoFull.setTitle(event.getTitle());
        eventDtoFull.setViews(event.getViews());
        return eventDtoFull;
    }

    public static List<EventDtoFull> toEventDtoFull(List<Event> events) {
        List<EventDtoFull> eventsDtoFull = new ArrayList<>();
        for (Event event : events) {
            eventsDtoFull.add(toEventDtoFull(event));
        }
        return eventsDtoFull;
    }

    public static EventDtoShort toEventDtoShort(Event event) {
        EventDtoShort eventDtoShort = new EventDtoShort();
        eventDtoShort.setId(event.getId());
        eventDtoShort.setAnnotation(event.getAnnotation());
        eventDtoShort.setCategory(new EventDtoShort.Category(
                event.getCategory().getId(),
                event.getCategory().getName()));
        eventDtoShort.setConfirmedRequests(event.getConfirmedRequests());
        eventDtoShort.setEventDate(event.getEventDate());
        eventDtoShort.setInitiator(new EventDtoShort.User(
                event.getInitiator().getId(),
                event.getInitiator().getName()));
        eventDtoShort.setPaid(event.getPaid());
        eventDtoShort.setTitle(event.getTitle());
        eventDtoShort.setViews(event.getViews());
        return eventDtoShort;
    }

    public static List<EventDtoShort> toEventDtoShort(List<Event> events) {
        List<EventDtoShort> eventDtoShorts = new ArrayList<>();
        for (Event event : events) {
            eventDtoShorts.add(toEventDtoShort(event));
        }
        return eventDtoShorts;
    }
}
