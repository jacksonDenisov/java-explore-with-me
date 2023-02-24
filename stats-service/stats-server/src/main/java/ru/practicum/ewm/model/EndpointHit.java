package ru.practicum.ewm.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "endpoint_hit")
@Getter
@Setter
@ToString
public class EndpointHit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nullable
    @Length(max = 50)
    private String app;

    @Nullable
    @Length(max = 200)
    private String uri;

    @Nullable
    @Length(max = 50)
    private String ip;

    private LocalDateTime timestamp;
}