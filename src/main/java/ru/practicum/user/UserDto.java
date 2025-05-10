package ru.practicum.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    @NotNull(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;
}
