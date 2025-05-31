package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserValidator;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserValidatorTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidUser() {
        User user = new User();
        user.setName("Valid User");
        user.setEmail("valid@email.com");

        assertTrue(userValidator.validate(user));
    }

    @Test
    void testNullEmail() {
        User user = new User();
        user.setName("User ");
        user.setEmail(null);

        ValidationException exception = assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertEquals("Адрес электронной почты не может быть пустым.", exception.getMessage());
    }

    @Test
    void testEmptyEmail() {
        User user = new User();
        user.setName("User ");
        user.setEmail("");

        ValidationException exception = assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertEquals("Адрес электронной почты не может быть пустым.", exception.getMessage());
    }

    @Test
    void testInvalidEmailFormat() {
        User user = new User();
        user.setName("User ");
        user.setEmail("invalid-email");

        ValidationException exception = assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertEquals("Введен неверный адрес электронной почты.", exception.getMessage());
    }

    @Test
    void testNullName() {
        User user = new User();
        user.setName(null);
        user.setEmail("user@example.com");

        ValidationException exception = assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertEquals("Имя пользователя не может быть пустым.", exception.getMessage());
    }

    @Test
    void testEmptyName() {
        User user = new User();
        user.setName("");
        user.setEmail("user@example.com");

        ValidationException exception = assertThrows(ValidationException.class, () -> userValidator.validate(user));
        assertEquals("Имя пользователя не может быть пустым.", exception.getMessage());
    }

    @Test
    void testDuplicateEmailValidation() {
        User user = new User();
        user.setEmail("duplicate@example.com");

        when(userRepository.findAll()).thenReturn(List.of(new User(1L, "Existing User", "duplicate@example.com")));

        assertTrue(userValidator.duplicateEmailValidation(user));
    }

    @Test
    void testNoDuplicateEmailValidation() {
        User user = new User();
        user.setEmail("unique@example.com");

        when(userRepository.findAll()).thenReturn(List.of(new User(1L, "Existing User", "duplicate@example.com")));

        assertFalse(userValidator.duplicateEmailValidation(user));
    }
}
