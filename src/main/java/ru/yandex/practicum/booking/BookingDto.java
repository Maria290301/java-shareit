package ru.yandex.practicum.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.item.ItemDto;
import ru.yandex.practicum.user.UserDto;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime  startDate;
    private LocalDateTime endDate;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
}
