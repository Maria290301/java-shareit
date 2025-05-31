package ru.practicum.shareit.itemTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ItemRepositoryTests {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder().name("User").email("user@example.com").build());
    }

    @Test
    void findByOwnerIdTest() {
        Item item = new Item(null, "Item Name", "Item Description", true, user);
        itemRepository.save(item);

        List<Item> items = itemRepository.findByOwnerId(user.getId());
        assertEquals(1, items.size());
        assertEquals("Item Name", items.get(0).getName());
    }

    @Test
    void searchAvailableItemsTest() {
        Item item = new Item(null, "Available Item", "Description", true, user);
        itemRepository.save(item);

        List<Item> items = itemRepository.searchAvailableItems("Available");
        assertEquals(1, items.size());
        assertEquals("Available Item", items.get(0).getName());
    }
}
