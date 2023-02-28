package ru.practicum.ewm.service.participation_request;

import ru.practicum.ewm.model.participation_request.ParticipationRequestDto;
import ru.practicum.ewm.model.participation_request.ParticipationRequestDtoStatusUpdate;
import ru.practicum.ewm.model.participation_request.ParticipationRequestDtoUpdated;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto createParticipationRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> findParticipationRequestByRequesterId(Long userId);

    ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> findParticipationRequestsForEventInitiator(Long initiatorId, Long eventId);

    ParticipationRequestDtoUpdated updateParticipationRequestStatusByEventInitiator(
            Long userId, Long eventId, ParticipationRequestDtoStatusUpdate participationRequestDtoStatusUpdate);
}
