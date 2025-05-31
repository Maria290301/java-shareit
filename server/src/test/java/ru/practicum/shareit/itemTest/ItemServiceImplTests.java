package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsAndComments;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceImplTests {

    @Autowired
    private ItemServiceImpl itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder().name("User ").email("user@example.com").build());
    }

    @Test
    void addItemTest() {
        ItemDto itemDto = new ItemDto(null, "Item Name", "Item Description", true, null, null);
        ItemDto createdItem = itemService.addItem(itemDto, user.getId());

        assertNotNull(createdItem);
        assertEquals("Item Name", createdItem.getName());
        assertEquals(user.getId(), createdItem.getOwnerId());
    }

    @Test
    void updateItemTest() {
        ItemDto itemDto = new ItemDto(null, "Item Name", "Item Description", true, null, null);
        ItemDto createdItem = itemService.addItem(itemDto, user.getId());

        ItemDto updateDto = new ItemDto(createdItem.getId(), "Updated Name", null, null, null, null);
        ItemDto updatedItem = itemService.updateItem(updateDto, createdItem.getId(), user.getId());

        assertEquals("Updated Name", updatedItem.getName());
    }

    @Test
    void getItemByIdTest() {
        ItemDto itemDto = new ItemDto(null, "Item Name", "Item Description", true, null, null);
        ItemDto createdItem = itemService.addItem(itemDto, user.getId());

        ItemDtoBookingsAndComments itemDetails = itemService.getItemById(user.getId(), createdItem.getId());

        assertEquals(createdItem.getId(), itemDetails.getId());
        assertEquals("Item Name", itemDetails.getName());
    }

    @Test
    void getUserItemsTest() {
        ItemDto itemDto1 = new ItemDto(null, "Item 1", "Description 1", true, null, null);
        ItemDto itemDto2 = new ItemDto(null, "Item 2", "Description 2", true, null, null);
        itemService.addItem(itemDto1, user.getId());
        itemService.addItem(itemDto2, user.getId());

        List<ItemDtoBookingsAndComments> userItems = itemService.getUserItems(user.getId());

        assertEquals(2, userItems.size());
    }

    @Test
    void getSearchTest() {
        ItemDto itemDto = new ItemDto(null, "Unique Item", "Description", true, null, null);
        itemService.addItem(itemDto, user.getId());

        List<ItemDto> searchedItems = itemService.getSearch("Unique");

        assertEquals(1, searchedItems.size());
        assertEquals("Unique Item", searchedItems.get(0).getName());
    }
}
