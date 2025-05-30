package ru.practicum.shareit.itemRequest;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import java.util.ArrayList;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequest fromItemRequestDto(ItemRequestDto itemRequestDto) {
        return ItemRequest
                .builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto
                .builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(new ArrayList<>())
                .build();
    }
}