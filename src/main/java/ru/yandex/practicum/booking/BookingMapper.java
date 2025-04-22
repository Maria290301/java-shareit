package ru.yandex.practicum.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.item.ItemMapper;
import ru.yandex.practicum.user.UserMapper;


@Component
public class BookingMapper {
    private final ItemMapper itemMapper;
    private final UserMapper userMapper;

    @Autowired
    public BookingMapper(ItemMapper itemMapper, UserMapper userMapper) {
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
    }

    public BookingDto toBookingDto(Booking booking) {
        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());
        dto.setStartDate(booking.getStartDate());
        dto.setEndDate(booking.getEndDate());
        dto.setItem(itemMapper.toItemDto(booking.getItem()));
        dto.setBooker(userMapper.toUserDto(booking.getBooker()));
        dto.setStatus(booking.getStatus());
        return dto;
    }

    public Booking toEntity(BookingDto dto) {
        Booking booking = new Booking();
        booking.setId(dto.getId());
        booking.setStartDate(dto.getStartDate());
        booking.setEndDate(dto.getEndDate());
        booking.setItem(itemMapper.toEntity(dto.getItem()));
        booking.setBooker(userMapper.toEntity(dto.getBooker()));
        booking.setStatus(dto.getStatus());
        return booking;
    }
}
