package ru.practicum.ewm.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class EventDtoUpdateByUser {

    @Nullable
    @Size(min = 20, max = 2000)
    private String annotation;

    @Nullable
    @Positive
    private Long category;

    @Nullable
    @Size(min = 20, max = 7000)
    private String description;

    @Nullable
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Nullable
    private Location location;

    @Nullable
    private Boolean paid;

    @Nullable
    private Integer participantLimit;

    @Nullable
    private Boolean requestModeration;

    @Nullable
    private EventStateAction stateAction;

    @Nullable
    @Size(min = 3, max = 120)
    private String title;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Location {

        @NotNull
        private Float lat;

        @NotNull
        private Float lon;
    }
}
