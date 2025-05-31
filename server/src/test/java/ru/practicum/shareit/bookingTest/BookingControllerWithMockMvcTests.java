package ru.practicum.shareit.bookingTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerWithMockMvcTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    private ObjectMapper mapper = new ObjectMapper();

    private UserDto userDto;
    private BookingDto bookingDto;

    @BeforeEach
    void init() {
        userDto = UserDto.builder()
                .id(1L)
                .name("User  Name")
                .email("user@example.com")
                .build();

        bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 10, 24, 12, 30))
                .end(LocalDateTime.of(2023, 11, 10, 13, 0))
                .itemId(1L)
                .bookerId(1L)
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void addBookingTest() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setItemId(1L);

        ResponseBookingDto responseBookingDto = new ResponseBookingDto();
        responseBookingDto.setId(1L);

        when(bookingService.addBooking(any(BookingDto.class), anyLong())).thenReturn(responseBookingDto);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseBookingDto)));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        ResponseBookingDto responseBookingDto = new ResponseBookingDto();
        responseBookingDto.setId(1L);

        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(responseBookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseBookingDto)));
    }

    @Test
    void approveBookingTest() throws Exception {
        ResponseBookingDto responseBookingDto = new ResponseBookingDto();
        responseBookingDto.setId(1L);

        when(bookingService.patchBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(responseBookingDto);

        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(responseBookingDto)));
    }

    @Test
    void getAllUsersBookingsTest() throws Exception {
        ResponseBookingDto responseBookingDto = new ResponseBookingDto();
        responseBookingDto.setId(1L);

        when(bookingService.getAllUsersBookings(anyLong(), any())).thenReturn(List.of(responseBookingDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(responseBookingDto))));
    }
}
