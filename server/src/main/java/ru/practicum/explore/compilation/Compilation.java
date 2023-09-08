package ru.practicum.explore.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.event.model.Event;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "compilation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    @Column
    private String title;

    @Column
    private boolean pinned;

    @ManyToMany
    @JoinTable(
            name = "event_compilation",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> events;

}
