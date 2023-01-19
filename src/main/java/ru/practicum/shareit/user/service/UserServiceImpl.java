package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    //UserValidator userValidator = new UserValidator();

    /*GET /users - получение списка пользователей*/
    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = repository.findAll();
        return UserMapper.mapToUserDto(users);
    }

    /*POST /users - создание нового пользователя*/
    @Override
    public UserDto saveNewUser(UserDto userDto) {
        log.info("Получен новый userDto {}", userDto);
        UserValidator.isValidEmail(userDto);
        User user = repository.save(UserMapper.toUser(userDto));
        log.info("Сохранен новый user {}", user);
        return UserMapper.toUserDto(user);
    }

    /*PATCH /users/{userId} - обновление пользователя*/
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        log.info("Запрос на обновление userDto {}", userDto);

        log.info("Получение пользователя по id {}", userId);
        Optional<User> userOptional = repository.findById(userId);
        User user = UserValidator.isValidUser(userOptional);
        //User user = userValidator.isValidUser(userOptional);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
            log.info("Обновление имени на {}", user.getName());
        }

        if (userDto.getEmail() != null && userDto.getEmail().contains("@")) {
            user.setEmail(userDto.getEmail());
            log.info("Обновление email на {}", user.getEmail());
        }

        User updateUser = repository.save(user);
        log.info("Пользователь обновлен: {}", updateUser);
        return UserMapper.toUserDto(updateUser);
    }

    /*GET /users/{userId} - получение пользователя по userId*/
    @Override
    public UserDto getUserDtoById(long userId) {
        log.info("Запрос на получение пользователя по id {}", userId);

        Optional<User> userOptional = repository.findById(userId);
        User user = UserValidator.isValidUser(userOptional);
        //User user = userValidator.isValidUser(userOptional);

        return UserMapper.toUserDto(user);
    }

    /*DELETE /users/{userId} - удаление пользователя по userId*/
    @Override
    public void deleteUserById(long userId) {
        log.info("Запрос на удаление пользователя по id {}", userId);

        log.info("Получение пользователя по id {}", userId);
        Optional<User> userOptional = repository.findById(userId);
        User user = UserValidator.isValidUser(userOptional);
        repository.delete(user);
    }

}