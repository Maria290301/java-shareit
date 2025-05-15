package ru.practicum.request;

import lombok.Data;
import ru.practicum.user.UserDto;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {

    private Long id;

    private String description;

    private UserDto requestor;

    private LocalDateTime created;

}