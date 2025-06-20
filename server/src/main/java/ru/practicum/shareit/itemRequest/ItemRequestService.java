package ru.practicum.shareit.itemRequest;

import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDto> getAllByUser(Long userId);

    List<ItemRequestDto> getAll(int from, int size, Long userId);

    ItemRequestDto getById(Long requestId, Long userId);
}