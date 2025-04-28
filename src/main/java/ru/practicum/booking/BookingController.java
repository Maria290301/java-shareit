package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestBody BookingDto bookingDto,
                                    @RequestHeader("X-User-Id") Long userId) {
        log.info("Получен запрос на создание бронирования от пользователя {}", userId);
        return bookingService.createBooking(bookingDto, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable("bookingId") Long bookingId) {
        log.info("Получен запрос на получение бронирования с ID {}", bookingId);
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping("/user/{userId}")
    public List<BookingDto> getUserBookings(@PathVariable("userId") Long userId) {
        log.info("Получен запрос на получение бронирований пользователя {}", userId);
        return bookingService.getUserBookings(userId);
    }

    @GetMapping("/item/{itemId}")
    public List<BookingDto> getItemBookings(@PathVariable("itemId") Long itemId) {
        log.info("Получен запрос на получение бронирований предмета {}", itemId);
        return bookingService.getItemBookings(itemId);
    }

    @PatchMapping("/{bookingId}/status")
    public BookingDto updateBookingStatus(@PathVariable("bookingId") Long bookingId,
                                          @RequestParam("status") BookingStatus newStatus) {
        log.info("Получен запрос на обновление статуса бронирования {} до {}", bookingId, newStatus);
        return bookingService.updateBookingStatus(bookingId, newStatus);
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable("bookingId") Long bookingId) {
        log.info("Получен запрос на отмену бронирования {}", bookingId);
        bookingService.cancelBooking(bookingId);
    }

    @GetMapping("/check-availability")
    public boolean checkDateOverlap(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                                    @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                                    @RequestParam("itemId") Long itemId) {
        log.info("Проверка доступности предмета {} в период с {} по {}", itemId, start, end);
        return bookingService.checkDateOverlap(start, end, itemId);
    }
}
