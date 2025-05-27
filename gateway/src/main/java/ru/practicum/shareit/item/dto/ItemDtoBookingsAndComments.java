package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.booking.dto.ShortBookingDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDtoBookingsAndComments {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private ShortBookingDto lastBooking;
    private ShortBookingDto nextBooking;
    private List<CommentDto> comments;
}