package ru.practicum.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Autowired
    public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository,
                                  UserRepository userRepository,
                                  ItemRequestMapper itemRequestMapper) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
        this.itemRequestMapper = itemRequestMapper;
    }

    @Override
    public ItemRequestDto createRequest(ItemRequestDto requestDto) {
        ItemRequest itemRequest = itemRequestMapper.toEntity(requestDto);
        itemRequest = itemRequestRepository.save(itemRequest);
        return itemRequestMapper.toRequestDto(itemRequest);
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId);
        if (itemRequest == null) {
            throw new NotFoundException("Запрос не найден");
        }
        return itemRequestMapper.toRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequestsByUserId(Long userId) {
        User requestor = userRepository.findById(userId);
        if (requestor == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return itemRequestRepository.findByRequestor(requestor)
                .stream()
                .map(itemRequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> searchRequests(String text) {
        return itemRequestRepository.findByDescriptionContaining(text)
                .stream()
                .map(itemRequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto updateRequest(Long requestId, ItemRequestDto updatedRequest) {
        ItemRequest existingRequest = itemRequestRepository.findById(requestId);
        if (existingRequest == null) {
            throw new NotFoundException("Запрос не найден");
        }

        existingRequest.setDescription(updatedRequest.getDescription());
        itemRequestRepository.save(existingRequest);
        return itemRequestMapper.toRequestDto(existingRequest);
    }

    @Override
    public void deleteRequest(Long requestId) {
        ItemRequest existingRequest = itemRequestRepository.findById(requestId);
        if (existingRequest == null) {
            throw new NotFoundException("Запрос не найден");
        }
        itemRequestRepository.delete(requestId);
    }
}
