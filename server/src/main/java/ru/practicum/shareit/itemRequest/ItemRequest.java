package ru.practicum.shareit.itemRequest;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.User;


import java.time.LocalDateTime;

@Entity
@Table(name = "item_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Column(nullable = false)
    private LocalDateTime created = LocalDateTime.now();

    // Конструктор без id и created (для создания)
    public ItemRequest(String description, User requester) {
        this.description = description;
        this.requester = requester;
        this.created = LocalDateTime.now();
    }
}

