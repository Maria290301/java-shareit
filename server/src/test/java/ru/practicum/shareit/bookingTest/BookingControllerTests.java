package ru.practicum.shareit.bookingTest;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingControllerTests {

    @Autowired
    private BookingController bookingController;

    @Autowired
    private UserController userController;

    @Autowired
    private ItemController itemController;

    private ItemDto itemDto;
    private UserDto userDto;
    private UserDto userDto1;
    private BookingDto bookingDto;

    @BeforeEach
    void init() {
        itemDto = ItemDto.builder()
                .name("Item Name")
                .description("Item Description")
                .available(true)
                .build();

        userDto = UserDto.builder()
                .name("User Name")
                .email("user@example.com")
                .build();

        userDto1 = UserDto.builder()
                .name("User Name 1")
                .email("user1@example.com")
                .build();

        bookingDto = BookingDto.builder()
                .start(LocalDateTime.of(2023, 10, 24, 12, 30))
                .end(LocalDateTime.of(2023, 11, 10, 13, 0))
                .build();
    }

    @Test
    @Transactional
    void getBookingByIdTest() {
        UserDto owner = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, owner.getId());

        UserDto booker = userController.addUser(userDto1);
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        ResponseBookingDto booking = bookingController.addBooking(bookingDto, booker.getId());

        ResponseBookingDto response = bookingController.getBookingById(owner.getId(), booking.getId());
        assertEquals(booking.getId(), response.getId());
    }

    @Test
    @Transactional
    void getAllUsersBookingsTest() {
        UserDto owner = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, owner.getId());

        UserDto booker = userController.addUser(userDto1);
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        ResponseBookingDto booking = bookingController.addBooking(bookingDto, booker.getId());
        assertNotNull(booking);

        List<ResponseBookingDto> bookings = bookingController.getAllUsersBookings(booker.getId(), BookingState.ALL);
        assertEquals(1, bookings.size());
    }

    @Test
    void addBookingTest() {
        UserDto owner = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, owner.getId());

        UserDto booker = userController.addUser(userDto1);
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        ResponseBookingDto booking = bookingController.addBooking(bookingDto, booker.getId());
        assertEquals(1L, booking.getId());
    }

    @Test
    @Transactional
    void approveBookingTest() {
        UserDto owner = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, owner.getId());

        UserDto booker = userController.addUser(userDto1);
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        ResponseBookingDto booking = bookingController.addBooking(bookingDto, booker.getId());

        ResponseBookingDto response = bookingController.updateBooking(owner.getId(), booking.getId(), true);
        assertEquals(BookingStatus.APPROVED, response.getStatus());
    }

    @Test
    void addBookingWithUnavailableItemTest() {
        UserDto owner = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, owner.getId());
        item.setAvailable(false);

        itemController.patchItem(item, item.getId(), owner.getId());

        UserDto booker = userController.addUser(userDto1);
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));

        assertThrows(ItemIsNotAvailableException.class, () -> bookingController.addBooking(bookingDto, booker.getId()));
    }

    @Test
    void addBookingByOwnerTest() {
        UserDto owner = userController.addUser(userDto);
        ItemDto item = itemController.addItem(itemDto, owner.getId());
        bookingDto.setItemId(item.getId());

        assertThrows(NotFoundException.class, () -> bookingController.addBooking(bookingDto, owner.getId()));
    }
}
