package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto addUser(@RequestBody UserDto userDto) {
        log.info("Получен запрос POST /users");
        return userService.addUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUser(@RequestBody UserDto userDto, @PathVariable Long userId) {
        log.info("Получен запрос PATCH /users/{}", userId);
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Получен запрос DELETE /users/{}", userId);
        userService.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос GET /users");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("Получен запрос GET /users/{}", userId);
        return userService.getUserById(userId);
    }
}