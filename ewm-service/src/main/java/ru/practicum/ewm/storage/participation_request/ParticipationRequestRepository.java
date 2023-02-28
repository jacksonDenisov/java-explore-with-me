package ru.practicum.ewm.storage.participation_request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.participation_request.ParticipationRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequesterId(Long id);

    Optional<ParticipationRequest> findParticipationRequestByIdAndRequesterId(Long id, Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByIdIn(List<Long> eventIds);
}
