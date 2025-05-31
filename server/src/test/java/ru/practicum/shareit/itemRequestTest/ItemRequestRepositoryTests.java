package ru.practicum.shareit.itemRequestTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.itemRequest.ItemRequest;
import ru.practicum.shareit.itemRequest.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
public class ItemRequestRepositoryTests {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        itemRequestRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findAllByRequestorIdOrderByCreatedAscTest() {
        User user = userRepository.save(User.builder().name("name").email("email@email.com").build());
        itemRequestRepository.save(ItemRequest.builder().description("description").requestor(user).created(LocalDateTime.now()).build());

        List<ItemRequest> items = itemRequestRepository.findAllByRequestorIdOrderByCreatedAsc(user.getId());
        assertThat(items.size(), equalTo(1));
    }
}
