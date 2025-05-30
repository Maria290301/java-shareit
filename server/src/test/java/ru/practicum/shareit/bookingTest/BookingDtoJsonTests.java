package ru.practicum.shareit.bookingTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonTest
public class BookingDtoJsonTests {
    @Autowired
    JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 10, 24, 12, 30, 0)) // Секунды равны 0
                .end(LocalDateTime.of(2023, 11, 10, 13, 0, 0)) // Секунды равны 0
                .itemId(1L)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        // Форматируем даты с секундами
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String expectedStart = bookingDto.getStart().format(formatter);
        String expectedEnd = bookingDto.getEnd().format(formatter);

        // Проверяем значения с помощью AssertJ
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(expectedStart); // Ожидаем с секундами
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(expectedEnd); // Ожидаем с секундами
    }
}