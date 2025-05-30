package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class BookingRepositoryTests {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    void init() {
        user = User.builder()
                .name("User  Name")
                .email("user@example.com")
                .build();

        item = Item.builder()
                .name("Item Name")
                .description("Item Description")
                .available(true)
                .owner(user)
                .build();

        booking = Booking.builder()
                .start(LocalDateTime.of(2023, 10, 24, 12, 30))
                .end(LocalDateTime.of(2023, 11, 10, 13, 0))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void saveBookingTest() {
        userRepository.save(user);
        itemRepository.save(item);
        Booking savedBooking = bookingRepository.save(booking);
        assertNotNull(savedBooking.getId());
    }

    @Test
    void findAllByBookerIdTest() {
        userRepository.save(user);
        itemRepository.save(item);
        bookingRepository.save(booking);
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(user.getId());
        assertEquals(1, bookings.size());
    }
}
