package ru.practicum.shareit.item.validator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.security.InvalidParameterException;
import java.util.Optional;

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

    @SneakyThrows
    public static void isValidOwner(User user, Item item) {
        if (!user.getId().equals(item.getOwner().getId())) {
            log.info("Вещь с id {} не принадлежит пользователю с id {}", item.getOwner().getId(), user.getId());
            throw new NotFoundException("Вещь другого пользователя");
        }
    }

    @SneakyThrows
    public static void isAvailable(Item item) {
        if(!item.getAvailable()) {
            log.info("Вещь с id {} недоступна для бронирования, available = ", item.getId(), item.getAvailable());
            throw new InvalidParameterException("Вещь недоступна для бронирования");
        }
    }

    @SneakyThrows
    public static Item isValidItem(Optional<Item> item) {
        if (!item.isPresent()) {
            log.info("Вещь не найдена");
            throw new NotFoundException("Вещь не найдена");
        }

        log.info("Получена вещь {}", item);
        return item.get();
    }

    @SneakyThrows
    public static void isValidText(String text) {
        if (text == null || text.isEmpty() || text.isBlank()) {
            log.info("Пустое поле text");
            throw new InvalidParameterException("Комментарий не может быть пустым");
        }

        if (text.matches("[a-z]+[а-я]+\\d")) {
            log.info("Недопустимые символы в поле text: {}", text);
            throw new InvalidParameterException("Недопустимые символы в тексте комментария");
        }
    }

}
