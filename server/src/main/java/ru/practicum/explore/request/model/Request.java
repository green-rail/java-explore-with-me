package ru.practicum.explore.request.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column
    @CreationTimestamp
    private LocalDateTime created;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @Column
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

}
