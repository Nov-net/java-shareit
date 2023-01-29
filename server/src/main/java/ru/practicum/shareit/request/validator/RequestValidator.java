package ru.practicum.shareit.request.validator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.request.dto.RequestDtoShot;
import ru.practicum.shareit.request.model.Request;

import java.security.InvalidParameterException;
import java.util.Optional;

@Slf4j
public class RequestValidator {

    @SneakyThrows
    public static void isValidDescription(RequestDtoShot request) {
        if (request.getDescription() == null || request.getDescription().isEmpty() || request.getDescription().isBlank()) {
            log.info("Пустое поле description");
            throw new InvalidParameterException("Описание не может быть пустым");
        }
    }

    @SneakyThrows
    public static Request isValidRequest(Optional<Request> request) {
        if (!request.isPresent()) {
            log.info("Запрос не найден");
            return null;
        }

        log.info("Получен запрос {}", request);
        return request.get();
    }

    @SneakyThrows
    public static void isValidParameters(Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new InvalidParameterException("Некорректные параметры запроса");
        }
    }

}
