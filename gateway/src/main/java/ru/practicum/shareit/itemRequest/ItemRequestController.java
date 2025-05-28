package ru.practicum.shareit.itemRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-User-Id") Long userId,
                                                @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на создание запроса на предмет от пользователя {}", userId);
        return requestClient.createRequest(userId, itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-User-Id") Long userId,
                                                 @PathVariable("requestId") long requestId) {
        log.info("Получен запрос на получение запроса на предмет с ID {} от пользователя {}", requestId, userId);
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getUserRequests(@RequestHeader("X-User-Id") Long userId) {
        log.info("Получен запрос на получение запросов пользователя {}", userId);
        return requestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchRequests(@RequestParam("text") String text) {
        log.info("Получен запрос на поиск запросов на предметы с текстом '{}'", text);
        return requestClient.searchRequests(text);
    }

    @PatchMapping("/{requestId}")
    public ResponseEntity<Object> updateRequest(@RequestHeader("X-User-Id") Long userId,
                                                @PathVariable("requestId") Long requestId,
                                                @Valid @RequestBody ItemRequestDto updatedRequest) {
        log.info("Получен запрос на обновление запроса на предмет с ID {} от пользователя {}", requestId, userId);
        return requestClient.updateRequest(userId, requestId, updatedRequest);
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<Object> deleteRequest(@RequestHeader("X-User-Id") Long userId,
                                                @PathVariable("requestId") Long requestId) {
        log.info("Получен запрос на удаление запроса на предмет с ID {} от пользователя {}", requestId, userId);
        return requestClient.deleteRequest(requestId);
    }
}
