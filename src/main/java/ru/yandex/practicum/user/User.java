package ru.yandex.practicum.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(unique = true)
    private String name;

    @NotNull
    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    @Size(min = 8)
    private String password;
}
