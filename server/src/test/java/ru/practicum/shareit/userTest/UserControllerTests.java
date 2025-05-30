package ru.practicum.shareit.userTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTests {

    @Autowired
    private UserController userController;

    private UserDto user;

    @BeforeEach
    void init() {
        user = UserDto.builder()
                .name("name")
                .email("user@email.com")
                .build();
    }

    @Test
    void createTest() {
        UserDto userDto = userController.addUser(user);
        assertEquals(userDto.getId(), userController.getUserById(userDto.getId()).getId());
    }

    @Test
    void updateTest() {
        userController.addUser(user);
        UserDto updatedUserDto = UserDto.builder().name("update name").email("update@email.com").build();
        userController.patchUser(updatedUserDto, 1L);
        assertEquals(updatedUserDto.getEmail(), userController.getUserById(1L).getEmail());
    }

    @Test
    void updateByWrongUserTest() {
        assertThrows(NotFoundException.class, () -> userController.patchUser(user, 1L));
    }

    @Test
    void deleteTest() {
        UserDto userDto = userController.addUser(user);
        assertEquals(1, userController.getAllUsers().size());
        userController.deleteUser(userDto.getId());
        assertEquals(0, userController.getAllUsers().size());
    }

    @Test
    void getByWrongIdTest() {
        assertThrows(NotFoundException.class, () -> userController.getUserById(1L));
    }

    @Test
    void updateUserPartialNameTest() {
        UserDto createdUser  = userController.addUser (user);
        UserDto updatedUserDto = UserDto.builder().name("new name").build(); // Обновляем только имя
        userController.patchUser (updatedUserDto, createdUser .getId());

        UserDto fetchedUser  = userController.getUserById(createdUser .getId());
        assertEquals("new name", fetchedUser .getName());
        assertEquals(user.getEmail(), fetchedUser .getEmail()); // Email должен остаться прежним
    }

    @Test
    void updateUserPartialEmailTest() {
        UserDto createdUser  = userController.addUser (user);
        UserDto updatedUserDto = UserDto.builder().email("new@email.com").build(); // Обновляем только email
        userController.patchUser (updatedUserDto, createdUser .getId());

        UserDto fetchedUser  = userController.getUserById(createdUser .getId());
        assertEquals("new@email.com", fetchedUser .getEmail());
        assertEquals(user.getName(), fetchedUser .getName()); // Имя должно остаться прежним
    }
    @Test
    void deleteExistingUserTest() {
        UserDto userDto = userController.addUser (user);
        assertEquals(1, userController.getAllUsers().size()); // Убедитесь, что пользователь добавлен

        userController.deleteUser (userDto.getId()); // Удаляем пользователя

        assertEquals(0, userController.getAllUsers().size()); // Убедитесь, что пользователь удален
    }
    @Test
    void deleteNonExistentUserTest() {
        assertThrows(NotFoundException.class, () -> userController.deleteUser (999L)); // Используйте ID, который не существует
    }
    @Test
    void getNonExistentUserByIdTest() {
        assertThrows(NotFoundException.class, () -> userController.getUserById(999L)); // Используйте ID, который не существует
    }
}
