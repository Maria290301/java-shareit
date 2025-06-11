package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.practicum.shareit.user.UserMapper;

import static ru.practicum.shareit.itemRequest.ItemRequestMapper.fromItemRequestDto;
import static ru.practicum.shareit.itemRequest.ItemRequestMapper.toItemRequestDto;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserService userService;
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        User user = UserMapper.toEntity(userService.getUserById(userId));
        ItemRequest itemRequest = fromItemRequestDto(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        itemRequestRepository.save(itemRequest);

        return toItemRequestDto(itemRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getAllByUser(Long userId) {
        userService.getUserById(userId);
        List<ItemRequest> itemRequestList = itemRequestRepository
                .findAllByRequestorIdOrderByCreatedAsc(userId);

        return getItemRequestDtoList(itemRequestList);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getAll(int from, int size, Long userId) {
        userService.getUserById(userId);
        List<ItemRequest> itemRequestList = itemRequestRepository
                .findAllByRequestorIdIsNotOrderByCreatedAsc(userId, PageRequest.of(from, size));

        return getItemRequestDtoList(itemRequestList);
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDto getById(Long requestId, Long userId) {
        userService.getUserById(userId);
        ItemRequest itemRequest = itemRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new NotFoundException("ItemRequest not found"));
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);

        List<Item> items = itemRepository.findByRequestId(requestId);
        itemRequestDto.setItems(items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList()));

        return itemRequestDto;
    }

    private List<ItemRequestDto> getItemRequestDtoList(List<ItemRequest> itemRequestList) {
        List<Long> requestIds = itemRequestList.stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        List<Item> items = itemRepository.findByRequestIdIn(requestIds);

        Map<Long, List<Item>> itemsByRequestId = items.stream()
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));

        return itemRequestList.stream().map(itemRequest -> {
            ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
            List<Item> itemList = itemsByRequestId.get(itemRequest.getId());
            itemRequestDto.setItems(itemList == null ? Collections.emptyList() :
                    itemList.stream().map(ItemMapper::toItemDto).collect(Collectors.toList()));
            return itemRequestDto;
        }).collect(Collectors.toList());
    }
}