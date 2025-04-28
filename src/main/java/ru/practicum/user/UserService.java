package ru.practicum.user;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);
}
