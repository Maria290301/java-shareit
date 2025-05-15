package ru.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ShortBookingDto {

    private Long id;
    private Long bookerId;
}