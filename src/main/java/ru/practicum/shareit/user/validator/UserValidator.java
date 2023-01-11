package ru.practicum.shareit.user.validator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.security.InvalidParameterException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserValidator {

    @SneakyThrows
    public static void isValidEmail(UserDto userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty() || userDto.getEmail().isBlank()
                || !userDto.getEmail().contains("@")) {
            log.info("Некорректный или пустой email");
            throw new InvalidParameterException("Некорректный или пустой email");
        }
    }

    @SneakyThrows
    public static User isValidUser(Optional<User> user) {
        if (!user.isPresent()) {
            log.info("Пользователь не найден");
            throw new NotFoundException("Пользователь не найден");
        }
        return user.get();
    }

}
