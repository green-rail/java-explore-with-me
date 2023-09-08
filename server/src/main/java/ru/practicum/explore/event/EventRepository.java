package ru.practicum.explore.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.category.Category;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventState;
import ru.practicum.explore.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByIdAndState(Long id, EventState state);

    @Query("select e " +
            "from Event e " +
            "WHERE e.state = :state " +
            "AND (:search IS NULL OR e.annotation LIKE %:search% OR e.description LIKE %:search%) " +
            "AND e.eventDate > :start AND e.eventDate < :end")
    Page<Event> findBySearch(@Param("state") EventState state,
                             @Param("search") String search,
                             @Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end,
                             Pageable page);

    @Query("select e " +
            "from Event e " +
            "WHERE e.state = :state " +
            "AND (:search IS NULL OR e.annotation LIKE %:search% OR e.description LIKE %:search%) " +
            "AND e.eventDate > :start AND e.eventDate < :end " +
            "AND ((select count(*) from Request r where r.event = e and r.status = 'CONFIRMED') < e.participantLimit)")
    Page<Event> findBySearchOnlyAvailable(@Param("state") EventState state,
                                          @Param("search") String search,
                                          @Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          Pageable page);

    @Query("select e " +
            "from Event e " +
            "WHERE e.state = :state " +
            "AND (:search IS NULL OR e.annotation LIKE %:search% OR e.description LIKE %:search%) " +
            "AND e.eventDate > :start AND e.eventDate < :end " +
            "AND e.paid = :paid")
    Page<Event> findBySearchAndPaid(@Param("state") EventState state,
                                    @Param("search") String search,
                                    @Param("paid") boolean paid,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    Pageable page);

    @Query("select e " +
           "from Event e " +
           "WHERE e.state = :state " +
           "AND (:search IS NULL OR e.annotation LIKE %:search% OR e.description LIKE %:search%) " +
           "AND e.eventDate > :start AND e.eventDate < :end " +
           "AND ((select count(*) from Request r where r.event = e and r.status = 'CONFIRMED') < e.participantLimit)" +
           "AND e.paid = :paid")
    Page<Event> findBySearchAndPaidOnlyAvailable(@Param("state") EventState state,
                                                 @Param("search") String search,
                                                 @Param("paid") boolean paid,
                                                 @Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end,
                                                 Pageable page);

    @Query("select e " +
            "from Event e " +
            "WHERE " +
            "(:userIds IS NULL OR e.initiator.id IN :userIds) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categoryIds IS NULL OR e.category.id IN :categoryIds) " +
            "AND e.eventDate > :start AND e.eventDate < :end")
    Page<Event> findByUserIdsStatesCategories(@Param("userIds") List<Long> userIds,
                                              @Param("states") Set<EventState> states,
                                              @Param("categoryIds") List<Long> categoryIds,
                                              @Param("start") LocalDateTime start,
                                              @Param("end") LocalDateTime end, Pageable page);

    Page<Event> findByInitiator(User user, Pageable page);

    boolean existsByCategory(Category category);

}
