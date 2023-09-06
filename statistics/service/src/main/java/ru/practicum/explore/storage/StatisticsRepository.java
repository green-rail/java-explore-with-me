package ru.practicum.explore.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT NEW ru.practicum.explore.model.ViewStats(e.app, e.uri, COUNT(e)) " +
            "FROM EndpointHit e " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e) DESC")
    List<ViewStats> findViewStats();

    @Query("SELECT NEW ru.practicum.explore.model.ViewStats(e.app, e.uri, COUNT(e)) " +
           "FROM EndpointHit e " +
           "WHERE e.timestamp > :start and e.timestamp < :end " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT(e) DESC")
    List<ViewStats> findViewStats(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT NEW ru.practicum.explore.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
           "FROM EndpointHit e " +
           "WHERE e.timestamp > :start and e.timestamp < :end " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findViewStatsUniqueIps(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT NEW ru.practicum.explore.model.ViewStats(e.app, e.uri, COUNT(e.ip)) " +
           "FROM EndpointHit e " +
           "WHERE e.timestamp > :start and e.timestamp < :end " +
           "AND e.uri IN :uris " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> findViewStatsByUris(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end,
                                        @Param("uris") List<String> uris);

    @Query("SELECT NEW ru.practicum.explore.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp > :start and e.timestamp < :end " +
            "AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findViewStatsByUrisUniqueIps(@Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end,
                                                 @Param("uris") List<String> uris);

}
