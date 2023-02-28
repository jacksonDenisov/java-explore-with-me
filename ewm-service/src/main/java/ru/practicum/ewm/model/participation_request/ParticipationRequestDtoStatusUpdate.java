package ru.practicum.ewm.model.participation_request;

import lombok.Data;

import java.util.List;

@Data
public class ParticipationRequestDtoStatusUpdate {

    List<Long> requestIds;

    ParticipationRequestState status;
}
