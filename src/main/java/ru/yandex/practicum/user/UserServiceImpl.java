package ru.yandex.practicum.user;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.exception.ConflictException;
import ru.yandex.practicum.exception.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        validateUserUniqueness(userDto.getName(), userDto.getEmail());

        if (userDto.getEmail() == null || userDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email не может быть пустым");
        }

        if (userDto.getName() == null || userDto.getName().isEmpty()) {
            userDto.setName(userDto.getEmail());
        }

        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }

        if (!Objects.equals(user.getName(), userDto.getName()) ||
                !Objects.equals(user.getEmail(), userDto.getEmail())) {
            validateUserUniqueness(userDto.getName(), userDto.getEmail());
        }

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        User updatedUser = userRepository.save(user);
        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден");
        }
        return userMapper.toUserDto(user);
    }

    private void validateUserUniqueness(String name, String email) {
        if (userRepository.existsByName(name)) {
            throw new ConflictException("Name уже используется");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email уже используется");
        }
    }
}
