package ru.practicum.user;

import org.springframework.stereotype.Repository;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.incrementAndGet());
            users.put(user.getId(), user);
        } else {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
            } else {
                throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден");
            }
        }
        return user;
    }

    public User findById(Long id) {
        return users.get(id);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public void deleteById(Long id) {
        users.remove(id);
    }

    public boolean existsByEmail(String email) {
        return users.values().stream()
                .anyMatch(u -> u.getEmail() != null && u.getEmail().equals(email));
    }

    public boolean existsByName(String name) {
        return users.values().stream()
                .anyMatch(u -> u.getName() != null && u.getName().equals(name));
    }
}
