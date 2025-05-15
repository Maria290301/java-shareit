package ru.practicum.booking;

import java.util.List;

public interface BookingService {

    ResponseBookingDto addBooking(BookingDto bookingDto, Long bookerId);

    ResponseBookingDto patchBooking(Long ownerId, Long bookingId, boolean isApproved);

    ResponseBookingDto getBookingById(Long requesterId, Long bookingId);

    List<ResponseBookingDto> getAllUsersBookings(Long usersId, BookingState state);

    List<ResponseBookingDto> getAllItemOwnerBookings(Long ownerId, BookingState state);
}