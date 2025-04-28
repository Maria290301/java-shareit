package ru.practicum.item;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDto getItem(Long itemId);

    ItemDto updateItem(Long itemId, ItemDto itemDto, Long userId);

    void deleteItem(Long itemId);

    List<ItemDto> getAllItemsByOwner(Long ownerId);

    List<ItemDto> searchItems(String text);
}
