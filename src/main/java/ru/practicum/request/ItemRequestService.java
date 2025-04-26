package ru.practicum.request;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createRequest(ItemRequestDto requestDto);

    ItemRequestDto getRequestById(Long requestId);

    List<ItemRequestDto> getRequestsByUserId(Long userId);

    List<ItemRequestDto> searchRequests(String text);

    ItemRequestDto updateRequest(Long requestId, ItemRequestDto updatedRequest);

    void deleteRequest(Long requestId);
}
