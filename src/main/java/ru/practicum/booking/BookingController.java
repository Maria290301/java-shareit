package ru.practicum.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseBookingDto addBooking(@RequestBody @Valid BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        log.info("Получен запрос POST /bookings");
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long bookingId, @RequestParam(name = "approved") boolean isApproved) {
        log.info("Получен запрос PATCH /bookings/{bookingId}");
        return bookingService.patchBooking(ownerId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long requesterId, @PathVariable Long bookingId) {
        log.info("Получен запрос GET /bookings/{bookingId}");
        return bookingService.getBookingById(requesterId, bookingId);
    }

    @GetMapping
    public List<ResponseBookingDto> getAllUsersBookings(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestParam(value = "state", defaultValue = "ALL", required = false) BookingState state) {
        log.info("Получен запрос GET /bookings?state={state}");
        return bookingService.getAllUsersBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getAllItemOwnerBookings(@RequestHeader("X-Sharer-User-Id") Long ownerId, @RequestParam(value = "state", defaultValue = "ALL", required = false) BookingState state) {
        log.info("Получен запрос GET /bookings/owner?state={state}");
        return bookingService.getAllItemOwnerBookings(ownerId, state);
    }
}