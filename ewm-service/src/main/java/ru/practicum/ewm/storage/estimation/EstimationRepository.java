package ru.practicum.ewm.storage.estimation;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.estimations.Estimation;

import java.util.List;
import java.util.Optional;

public interface EstimationRepository extends JpaRepository<Estimation, Long> {

    Optional<Estimation> findByEventIdAndUserId(Long eventId, Long userId);

    List<Estimation> findAllByEventId(Long eventId);
}
