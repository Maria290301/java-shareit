package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;

@Slf4j
@Component
public class UserValidator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean validate(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Адрес электронной почты не может быть пустым.");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Введен неверный адрес электронной почты.");
            throw new ValidationException("Введен неверный адрес электронной почты.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя не может быть пустым.");
            throw new ValidationException("Имя пользователя не может быть пустым.");
        }
        return true;
    }

    public boolean duplicateEmailValidation(User user) {
        return userRepository.findAll().stream()
                .anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()));
    }
}