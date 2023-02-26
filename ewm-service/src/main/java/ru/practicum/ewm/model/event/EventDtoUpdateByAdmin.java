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

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventDtoUpdateByAdmin {

    @Nullable
    private String annotation;

    @Nullable
    private Long category;

    @Nullable
    private String description;

    @Nullable
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @Nullable
    private EventDtoUpdateByUser.Location location;

    @Nullable
    private Boolean paid;

    @Nullable
    private Integer participantLimit;

    @Nullable
    private Boolean requestModeration;

    @Nullable
    private EventStateAction stateAction;

    @Nullable
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
