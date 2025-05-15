package ru.practicum.item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    ItemDtoBookingsAndComments getItemById(Long ownerId, Long itemId);

    List<ItemDtoBookingsAndComments> getUserItems(Long ownerId);

    List<ItemDto> getSearch(String text);

    CommentDto addComment(CommentDto commentDto, Long itemId, Long authorId);
}
