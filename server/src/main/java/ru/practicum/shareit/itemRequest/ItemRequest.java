package ru.practicum.shareit.itemRequest;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.User;


import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "requests")
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description", nullable = false)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;
    private LocalDateTime created;

    public ItemRequest() {
    }
}
