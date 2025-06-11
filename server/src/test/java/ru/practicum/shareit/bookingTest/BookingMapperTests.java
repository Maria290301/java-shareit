package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class BookingMapperTests {

    @Test
    void toBookingTest() {
        BookingDto dto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        Item item = new Item();
        User booker = new User();

        Booking booking = BookingMapper.toBooking(dto, item, booker);

        assertNotNull(booking);
        assertEquals(1L, booking.getId());
        assertEquals(item, booking.getItem());
        assertEquals(booker, booking.getBooker());
    }

    @Test
    void toBookingTest_IdNull() {
        BookingDto dto = BookingDto.builder()
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();
        Item item = new Item();
        User booker = new User();

        Booking booking = BookingMapper.toBooking(dto, item, booker);

        assertNotNull(booking);
        assertNull(booking.getId());
    }

    @Test
    void toResponseBookingDtoTest() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        User booker = new User();
        booker.setId(2L);

        Booking booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        ResponseBookingDto responseDto = BookingMapper.toResponseBookingDto(booking);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals(item.getId(), responseDto.getItem().getId());
        assertEquals(booker.getId(), responseDto.getBooker().getId());
        assertEquals(BookingStatus.WAITING, responseDto.getStatus());
    }

    @Test
    void listToResponseBookingDtoTest() {
        User owner = new User();
        owner.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setOwner(owner);

        User booker = new User();
        booker.setId(2L);

        Booking booking = Booking.builder()
                .id(1L) // Установите id
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        List<Booking> bookings = List.of(booking);

        List<ResponseBookingDto> responseDtos = BookingMapper.listToResponseBookingDto(bookings);

        assertNotNull(responseDtos);
        assertEquals(1, responseDtos.size());
        assertEquals(1L, responseDtos.get(0).getId());
        assertEquals(item.getId(), responseDtos.get(0).getItem().getId());
        assertEquals(booker.getId(), responseDtos.get(0).getBooker().getId());
    }

    @Test
    void listToResponseBookingDtoTest_EmptyList() {
        List<ResponseBookingDto> responseDtos = BookingMapper.listToResponseBookingDto(Collections.emptyList());

        assertNotNull(responseDtos);
        assertTrue(responseDtos.isEmpty());
    }
}
