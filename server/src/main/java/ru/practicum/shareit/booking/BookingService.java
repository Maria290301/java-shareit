package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import java.util.List;

public interface BookingService {

    ResponseBookingDto addBooking(BookingDto bookingDto, Long bookerId);

    ResponseBookingDto patchBooking(Long ownerId, Long bookingId, boolean isApproved);

    ResponseBookingDto getBookingById(Long requesterId, Long bookingId);

    List<ResponseBookingDto> getAllUsersBookings(Long usersId, BookingState state);

    List<ResponseBookingDto> getAllItemOwnerBookings(Long ownerId, BookingState state);
}