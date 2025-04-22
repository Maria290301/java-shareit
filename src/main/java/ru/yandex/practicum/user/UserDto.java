package ru.yandex.practicum.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String name;

    @Email
    private String email;
    private String password;
}
