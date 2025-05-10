package ru.practicum.request;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
public class ItemRequestDto {

    private Long id;

    private String description;

    private UserDto requestor;

    private LocalDateTime created;

}