package ru.practicum.ewm.model.participation_request;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests")
@Getter
@Setter
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column(length = 30)
    @Enumerated(EnumType.STRING)
    private ParticipationRequestState status;

    private LocalDateTime created;
}
