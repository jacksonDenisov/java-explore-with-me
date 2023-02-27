package ru.practicum.ewm.model.participation_request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ParticipationRequestDto {

    private Long id;

    private Long event;

    private Long requester;

    private ParticipationRequestState status;

    private LocalDateTime created;
}
