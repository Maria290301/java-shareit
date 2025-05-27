package ru.practicum.shareit.itemRequest;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.itemRequest.dto.ItemResponseDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest request, List<Item> items) {
        List<ItemResponseDto> itemDtos = items.stream()
                .map(i -> new ItemResponseDto(i.getId(), i.getName(), i.getOwner().getId()))
                .collect(Collectors.toList());
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                itemDtos
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto dto, User user) {
        return new ItemRequest(dto.getDescription(), user);
    }
}
