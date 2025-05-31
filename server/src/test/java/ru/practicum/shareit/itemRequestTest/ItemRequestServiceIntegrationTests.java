package ru.practicum.shareit.itemRequestTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.itemRequest.ItemRequestRepository;
import ru.practicum.shareit.itemRequest.ItemRequestService;
import ru.practicum.shareit.itemRequest.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceIntegrationTests {
    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private UserDto user;

    @BeforeEach
    void setUp() {
        user = userService.addUser(UserDto.builder().name("Test User").email("test@example.com").build());
    }

    @Test
    void createItemRequestTest() {
        ItemRequestDto requestDto = ItemRequestDto.builder().description("Test request").build();
        ItemRequestDto createdRequest = itemRequestService.create(user.getId(), requestDto);
        assertNotNull(createdRequest.getId());
        assertEquals("Test request", createdRequest.getDescription());
    }

    @Test
    void getAllByUserTest() {
        ItemRequestDto requestDto = ItemRequestDto.builder().description("Test request").build();
        itemRequestService.create(user.getId(), requestDto);
        List<ItemRequestDto> requests = itemRequestService.getAllByUser(user.getId());
        assertEquals(1, requests.size());
        assertEquals("Test request", requests.get(0).getDescription());
    }
}
