package ru.practicum.ewm.model.estimations;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.user.User;

import javax.persistence.*;

@Entity
@Table(name = "estimations")
@Getter
@Setter
public class Estimation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "estimation_type", length = 15)
    @Enumerated(EnumType.STRING)
    private EstimationType estimationType;
}
