package ru.practicum.explore.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.request.model.RequestStatus;
import ru.practicum.explore.user.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select r " +
            "from Request r " +
            "WHERE r.requester = :user")
    List<Request> findByUserId(@Param("user") User user);

    @Query("select r " +
            "from Request r " +
            "WHERE r.requester.id = :userId AND r.event.id = :eventId")
    Optional<Request> findByUserIdAndEventId(@Param("userId") Long userId, @Param("eventId") Long eventId);

    @Query("select r " +
            "from Request r " +
            "WHERE r.event.id = :eventId")
    List<Request> findByEventId(@Param("eventId") Long eventId);


    int countByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findAllByEventId(Long eventId);

}
