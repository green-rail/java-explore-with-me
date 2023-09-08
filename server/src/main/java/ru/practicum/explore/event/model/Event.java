package ru.practicum.explore.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import ru.practicum.explore.category.Category;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ToString.Exclude
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Request> requests;

    @Column(name = "created_on")
    @CreationTimestamp
    private LocalDateTime createdOn;

    @Column
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon")),
    })
    private Location location;

    @Column
    private boolean paid;

    @Column(name = "participant_limit")
    private int participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private boolean requestModeration;

    @Column
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column
    private String title;


    public int getRequestCount() {
        if (getRequests() == null) {
            return 0;
        }
        return requests.size();
    }
}
