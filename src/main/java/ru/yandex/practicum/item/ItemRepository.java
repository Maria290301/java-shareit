package ru.yandex.practicum.item;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.exception.NotFoundException;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();


    public Item save(Item item) {
        if (item.getId() == null) {
            item.setId(idCounter.incrementAndGet());
            items.put(item.getId(), item);
        } else {
            if (items.containsKey(item.getId())) {
                items.put(item.getId(), item);
            } else {
                throw new NotFoundException("Item with ID " + item.getId() + " not found");
            }
        }
        return item;
    }

    public Item findById(Long id) {
        return items.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(items.values());
    }

    public void deleteById(Long id) {
        items.remove(id);
    }

    public List<Item> findByOwnerId(Long ownerId) {
        return items.values()
                .stream()
                .filter(i -> i.getOwner().getId().equals(ownerId))
                .collect(Collectors.toList());
    }

    public List<Item> search(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyList();
        }

        return items.values().stream()
                .filter(i -> i != null &&
                        i.isAvailable() &&
                        (StringUtils.isNotEmpty(i.getName()) && i.getName().toLowerCase().contains(text.toLowerCase()) ||
                                StringUtils.isNotEmpty(i.getDescription()) && i.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }
}
