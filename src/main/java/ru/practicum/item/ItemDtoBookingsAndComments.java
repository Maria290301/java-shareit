package ru.practicum.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.booking.ShortBookingDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemDtoBookingsAndComments {

    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;

    private ShortBookingDto lastBooking;

    private ShortBookingDto nextBooking;

    private List<CommentDto> comments;

    public ItemDtoBookingsAndComments() {

    }
}