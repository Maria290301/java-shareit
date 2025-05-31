package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsAndComments;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, Long ownerId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long ownerId);

    ItemDtoBookingsAndComments getItemById(Long ownerId, Long itemId);

    List<ItemDtoBookingsAndComments> getUserItems(Long ownerId);

    List<ItemDto> getSearch(String text);

    CommentDto addComment(CommentDto commentDto, Long itemId, Long authorId);
}
