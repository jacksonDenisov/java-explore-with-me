package ru.practicum.ewm.storage.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewm.model.event.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    List<Event> findAllByIdIn(List<Long> ids);

    @Query("SELECT AVG(e.rating) FROM Event e WHERE e.initiator.id = ?1")
    Double findAvgInitiatorRating(Long initiatorId);
}
