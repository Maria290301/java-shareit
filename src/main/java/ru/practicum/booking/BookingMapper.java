package ru.practicum.booking;

import org.springframework.stereotype.Component;
import ru.practicum.item.Item;
import ru.practicum.item.ItemMapper;
import ru.practicum.user.User;
import ru.practicum.user.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {


    public static Booking toBooking(BookingDto dto, Item item, User booker) {
        return new Booking(
                dto.getId() != null ? dto.getId().longValue() : null,
                dto.getStart(),
                dto.getEnd(),
                item,
                booker,
                BookingStatus.WAITING
        );
    }

    public static ResponseBookingDto toResponseBookingDto(Booking booking) {
        return new ResponseBookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.toItemDto(booking.getItem()),
                UserMapper.toUserDto(booking.getBooker()),
                booking.getStatus()
        );
    }

    public static List<ResponseBookingDto> listToResponseBookingDto(Iterable<Booking> bookings) {
        List<ResponseBookingDto> result = new ArrayList<>();
        for (Booking b : bookings) {
            result.add(toResponseBookingDto(b));
        }
        return result;
    }
}