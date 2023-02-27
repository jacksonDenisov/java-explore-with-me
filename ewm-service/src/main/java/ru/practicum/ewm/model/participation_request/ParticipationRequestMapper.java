package ru.practicum.ewm.model.participation_request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipationRequestMapper {

    public static ParticipationRequest toParticipationRequest(Event event, User requester,
                                                              ParticipationRequestState status,
                                                              LocalDateTime created) {
        ParticipationRequest participationRequest = new ParticipationRequest();
        participationRequest.setEvent(event);
        participationRequest.setRequester(requester);
        participationRequest.setStatus(status);
        participationRequest.setCreated(created);
        return participationRequest;
    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        ParticipationRequestDto participationRequestDto = new ParticipationRequestDto();
        participationRequestDto.setId(participationRequest.getId());
        participationRequestDto.setEvent(participationRequest.getEvent().getId());
        participationRequestDto.setRequester(participationRequest.getRequester().getId());
        participationRequestDto.setStatus(participationRequest.getStatus());
        participationRequestDto.setCreated(participationRequest.getCreated());
        return participationRequestDto;
    }

    public static List<ParticipationRequestDto> toParticipationRequestDto(List<ParticipationRequest> participationRequests) {
        List<ParticipationRequestDto> participationRequestDtos = new ArrayList<>();
        for (ParticipationRequest participationRequest : participationRequests) {
            participationRequestDtos.add(toParticipationRequestDto(participationRequest));
        }
        return participationRequestDtos;
    }
}
