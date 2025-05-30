package ru.practicum.shareit.itemRequestTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.itemRequest.ItemRequest;
import ru.practicum.shareit.itemRequest.ItemRequestRepository;
import ru.practicum.shareit.itemRequest.ItemRequestServiceImpl;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemRequestServiceImplTests {
    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    private User user;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).name("User ").email("user@example.com").build();
        itemRequestDto = ItemRequestDto.builder().description("Test request").build();
    }

    @Test
    void createTest() {
        when(userService.getUserById(user.getId())).thenReturn(UserMapper.toUserDto(user));
        when(itemRequestRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ItemRequestDto createdRequest = itemRequestService.create(user.getId(), itemRequestDto);

        assertNotNull(createdRequest);
        assertEquals(itemRequestDto.getDescription(), createdRequest.getDescription());
    }

    @Test
    void getAllByUserTest() {
        when(userService.getUserById(user.getId())).thenReturn(UserMapper.toUserDto(user));
        when(itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(user.getId())).thenReturn(List.of(
                ItemRequest.builder().id(1L).description("Request 1").requestor(user).created(LocalDateTime.now()).build()
        ));

        List<ItemRequestDto> requests = itemRequestService.getAllByUser(user.getId());

        assertEquals(1, requests.size());
        assertEquals("Request 1", requests.get(0).getDescription());
    }

    @Test
    void getAllTest() {
        when(userService.getUserById(user.getId())).thenReturn(UserMapper.toUserDto(user));
        when(itemRequestRepository.findAllByRequestorIdIsNotOrderByCreatedAsc(eq(user.getId()), any())).thenReturn(List.of(
                ItemRequest.builder().id(2L).description("Request 2").requestor(user).created(LocalDateTime.now()).build()
        ));

        List<ItemRequestDto> requests = itemRequestService.getAll(0, 10, user.getId());

        assertEquals(1, requests.size());
        assertEquals("Request 2", requests.get(0).getDescription());
    }

    @Test
    void getByIdTest() {
        ItemRequest itemRequest = ItemRequest.builder().id(1L).description("Request 1").requestor(user).created(LocalDateTime.now()).build();
        when(userService.getUserById(user.getId())).thenReturn(UserMapper.toUserDto(user));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
        when(itemRepository.findByRequestId(1L)).thenReturn(Collections.emptyList());

        ItemRequestDto requestDto = itemRequestService.getById(1L, user.getId());

        assertEquals("Request 1", requestDto.getDescription());
        assertTrue(requestDto.getItems().isEmpty());
    }

    @Test
    void getByIdNotFoundTest() {
        when(userService.getUserById(user.getId())).thenReturn(UserMapper.toUserDto(user));
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemRequestService.getById(1L, user.getId()));
    }

    @Test
    void createWithNonExistentUserTest() {
        when(userService.getUserById(anyLong())).thenThrow(new NotFoundException("User  not found"));

        assertThrows(NotFoundException.class, () -> {
            itemRequestService.create(999L, itemRequestDto); // ID несуществующего пользователя
        });
    }

    @Test
    void getAllByNonExistentUserTest() {
        when(userService.getUserById(anyLong())).thenThrow(new NotFoundException("User  not found"));

        assertThrows(NotFoundException.class, () -> {
            itemRequestService.getAllByUser(999L);
        });
    }

    @Test
    void getAllWithInvalidPaginationTest() {
        when(userService.getUserById(user.getId())).thenReturn(UserMapper.toUserDto(user));

        assertThrows(IllegalArgumentException.class, () -> {
            itemRequestService.getAll(-1, 10, user.getId());
        });

        assertThrows(IllegalArgumentException.class, () -> {
            itemRequestService.getAll(0, -1, user.getId());
        });
    }
}
