package ru.practicum.user;

import java.util.List;

public interface UserService {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    void deleteUser(Long userId);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long userId);
}