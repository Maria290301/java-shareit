package ru.yandex.practicum.request;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                        @RequestHeader("X-User-Id") Long userId) {
        log.info("Получен запрос на создание запроса на предмет от пользователя {}", userId);
        return itemRequestService.createRequest(itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@PathVariable("requestId") Long requestId) {
        log.info("Получен запрос на получение запроса на предмет с ID {}", requestId);
        return itemRequestService.getRequestById(requestId);
    }

    @GetMapping("/user/{userId}")
    public List<ItemRequestDto> getUserRequests(@PathVariable("userId") Long userId) {
        log.info("Получен запрос на получение запросов пользователя {}", userId);
        return itemRequestService.getRequestsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemRequestDto> searchRequests(@RequestParam("text") String text) {
        log.info("Получен запрос на поиск запросов на предметы с текстом '{}'", text);
        return itemRequestService.searchRequests(text);
    }

    @PatchMapping("/{requestId}")
    public ItemRequestDto updateRequest(@PathVariable("requestId") Long requestId,
                                        @Valid @RequestBody ItemRequestDto updatedRequest) {
        log.info("Получен запрос на обновление запроса на предмет с ID {} ", requestId);
        return itemRequestService.updateRequest(requestId, updatedRequest);
    }

    @DeleteMapping("/{requestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRequest(@PathVariable("requestId") Long requestId) {
        log.info("Получен запрос на удаление запроса на предмет с ID {}", requestId);
        itemRequestService.deleteRequest(requestId);
    }
}
