package ru.practicum.shareit.user.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private static final List<User> users = new ArrayList<>();
    private long lastId = 0;

    /*GET /users - получение списка пользователей*/
    @Override
    public List<UserDto> getAllUsers() {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    /*POST /users - создание нового пользователя*/
    @Override
    public UserDto saveNewUser(UserDto userDto) {
        log.info("Получен новый userDto {}", userDto);
        UserValidator.isValidEmail(userDto);
        userDto.setId(getId());
        log.info("Установлен id userDto {}", userDto.getId());
        User user = UserMapper.toUser(userDto);
        users.add(user);

        return userDto;
    }

    /*PATCH /users/{userId} - обновление пользователя*/
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        log.info("Обновление userDto {}", userDto);
        UserValidator.isNotExistingEmail(userDto);
        User user = findUserById(userId);
        users.remove(user);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && userDto.getEmail().contains("@")) {
            user.setEmail(userDto.getEmail());
        }

        users.add(user);
        return UserMapper.toUserDto(user);
    }

    /*GET /users/{userId} - получение пользователя по userId*/
    @Override
    public UserDto getUserDtoById(long userId) {
            return UserMapper.toUserDto(findUserById(userId));
    }

    /*DELETE /users/{userId} - удаление пользователя по userId*/
    @Override
    public void deleteUserById(long userId) {
        User user = findUserById(userId);
        if (user != null) {
            users.remove(user);
        }
    }

    /*поиск пользователя по userId*/
    public User findUserById(long id) {
        Optional<User> user = users.stream()
                .filter(u -> u.getId() == id)
                .findFirst();

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    /*приват - получение нового id*/
    private long getId() {
        lastId++;
        return lastId;
    }

    /*получение списка Email для валидации*/
    public static List<String> findEmailUsers() {
        return users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

}
