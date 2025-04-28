package ru.practicum.booking;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class BookingRepository {
    private final Map<Long, Booking> bookings = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public Booking save(Booking booking) {
        if (booking.getId() == null) {
            booking.setId(idCounter.incrementAndGet());
        }
        bookings.put(booking.getId(), booking);
        return booking;
    }

    public Booking findById(Long id) {
        return bookings.get(id);
    }

    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }

    public void deleteById(Long id) {
        bookings.remove(id);
    }

    public List<Booking> findByItemId(Long itemId) {
        return bookings.values().stream()
                .filter(b -> itemId.equals(b.getItem().getId()))
                .collect(Collectors.toList());
    }

    public List<Booking> findByBookerId(Long bookerId) {
        return bookings.values().stream()
                .filter(b -> b.getBooker().getId().equals(bookerId))
                .collect(Collectors.toList());
    }

    public List<Booking> findByStatus(BookingStatus status) {
        return bookings.values().stream()
                .filter(b -> b.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
