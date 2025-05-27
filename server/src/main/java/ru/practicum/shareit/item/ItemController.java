package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsAndComments;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос POST /items");
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                             @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос PATCH /items/{itemId}");
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoBookingsAndComments getItemById(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                  @PathVariable Long itemId) {
        log.info("Получен запрос GET /items/{itemId}");
        return itemService.getItemById(ownerId, itemId);
    }

    @GetMapping
    public List<ItemDtoBookingsAndComments> getUserItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос GET /items");
        return itemService.getUserItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearch(@RequestParam(value = "text") String text) {
        log.info("Получен запрос GET /search");
        return itemService.getSearch(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody @Valid CommentDto commentDto, @PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long authorId) {
        log.info("Получен запрос POST /items");
        return itemService.addComment(commentDto, itemId, authorId);
    }
}