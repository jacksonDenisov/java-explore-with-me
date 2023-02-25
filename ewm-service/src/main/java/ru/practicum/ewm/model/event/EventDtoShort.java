package ru.practicum.ewm.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class EventDtoShort {

    private Long id;

    private String annotation;

    private Category category;

    private Long confirmedRequests;

    private LocalDateTime eventDate;

    private User initiator;

    private Boolean paid;

    private String title;

    private Long views;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class User {

        private Long id;

        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Category {

        private Long id;

        private String name;
    }

}
