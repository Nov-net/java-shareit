package ru.practicum.shareit.item.validator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ItemDto;

import java.security.InvalidParameterException;

@Slf4j
public class ItemValidator {

    @SneakyThrows
    public static void isValidItem(ItemDto itemDto) {
        isValidName(itemDto);
        isValidDescription(itemDto);
        isValidAvailable(itemDto);
    }

    @SneakyThrows
    public static void isValidName(ItemDto itemDto) {
        if (itemDto.getName() == null || itemDto.getName().isEmpty() || itemDto.getName().isBlank()) {
            log.info("Пустое поле name");
            throw new InvalidParameterException("Название не может быть пустым");
        }
    }

    @SneakyThrows
    public static void isValidDescription(ItemDto itemDto) {
        if (itemDto.getDescription() == null || itemDto.getDescription().isEmpty() || itemDto.getDescription().isBlank()) {
            log.info("Пустое поле description");
            throw new InvalidParameterException("Описание не может быть пустым");
        }
    }

    @SneakyThrows
    public static void isValidAvailable(ItemDto itemDto) {
        if (itemDto.getAvailable() == null) {
            log.info("Пустое поле available");
            throw new InvalidParameterException("Статус доступности не может быть пустым");
        }
    }

}
