package ru.practicum.ewm.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.EndpointHit;
import ru.practicum.ewm.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT new ru.practicum.ewm.model.ViewStats(e.app as app, e.uri as uri, COUNT(e.ip) as hits) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 AND uri in ?3 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY uri DESC")
    List<ViewStats> findViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.ewm.model.ViewStats(e.app as app, e.uri as uri, COUNT(DISTINCT e.ip) as hits) " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 AND uri in ?3 " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY uri DESC")
    List<ViewStats> findViewStatsByUniqIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
