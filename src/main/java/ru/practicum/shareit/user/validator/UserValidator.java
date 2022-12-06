package ru.practicum.shareit.user.validator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepositoryImpl;

import java.security.InvalidParameterException;

@Slf4j
public class UserValidator {
    @SneakyThrows
    public static void isValidEmail(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty() || userDto.getEmail().isBlank()
                || !userDto.getEmail().contains("@")) {
            log.info("Некорректный или пустой email");
            throw new InvalidParameterException("Некорректный или пустой email");
        }

        isNotExistingEmail(userDto);
    }

    @SneakyThrows
    public static void isNotExistingEmail(UserDto userDto) {
        if(UserRepositoryImpl.findEmailUsers().contains(userDto.getEmail())) {
            log.info("Пользователь уже существует");
            throw new AlreadyExistException("Пользователь уже существует");
        }
    }

}
