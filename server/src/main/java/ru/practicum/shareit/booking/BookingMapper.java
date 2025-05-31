package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
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

    public static List<ResponseBookingDto> listToResponseBookingDto(List<Booking> bookings) {
        List<ResponseBookingDto> result = new ArrayList<>();
        for (Booking b : bookings) {
            result.add(toResponseBookingDto(b));
        }
        return result;
    }
}