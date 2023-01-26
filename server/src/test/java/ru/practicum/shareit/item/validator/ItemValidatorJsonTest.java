package ru.practicum.shareit.item.validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.dto.BookingShot;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.validator.BookingValidator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDtoShot;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JsonTest
public class ItemValidatorJsonTest {
    @Autowired
    private JacksonTester<Item> json;
    ItemValidator validator = new ItemValidator();
    List<Item> items = new ArrayList<>();
    LocalDateTime now = LocalDateTime.of(2023, 02, 15, 21, 07, 57);
    User user = new User(1L, "User 1", "1@ya.ru");
    User user2 = new User(2L, "User 2", "2@ya.ru");
    Request request = new Request(1L, "Описание", items, user, now);
    Item item = new Item(1L, "Вещь", "Описание", false, user, request);
    BookingDtoForItemDto booking = new BookingDtoForItemDto(1L, now, now, item, 1L, Status.APPROVED);
    CommentDto comment = new CommentDto(1L, "Комментарий", "Имя автора", now);
    ItemDto itemDto = new ItemDto(1L, "Вещь", "Описание", false, booking, booking, List.of(comment), 1L);

    @Test
    void isValidItemiTest() throws Exception {
        ItemDto itemDto1 = new ItemDto(1L, null, "Описание", true, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidItem(itemDto1));

        ItemDto itemDto2 = new ItemDto(1L, "Есть", null, true, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidItem(itemDto2));

        ItemDto itemDto3 = new ItemDto(1L, "Есть", "Описание", null, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidItem(itemDto3));
    }

    @Test
    void isValidNameTest() throws Exception {
        ItemDto itemDto1 = new ItemDto(1L, null, "Описание", true, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidName(itemDto1));

        ItemDto itemDto2 = new ItemDto(1L, "", "Описание", true, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidName(itemDto2));

        ItemDto itemDto3 = new ItemDto(1L, " ", "Описание", true, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidName(itemDto3));
    }

    @Test
    void isValidDescriptionTest() throws Exception {
        ItemDto itemDto1 = new ItemDto(1L, null, null, true, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidDescription(itemDto1));

        ItemDto itemDto2 = new ItemDto(1L, "", "", true, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidDescription(itemDto2));

        ItemDto itemDto3 = new ItemDto(1L, " ", " ", true, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidDescription(itemDto3));
    }

    @Test
    void isValidAvailableTest() throws Exception {
        ItemDto itemDto1 = new ItemDto(1L, null, null, null, booking, booking, List.of(comment), 1L);
        assertThrows(InvalidParameterException.class, () -> validator.isValidAvailable(itemDto1));
    }

    @Test
    void isValidOwnerTest() throws Exception {
        assertThrows(NotFoundException.class, () -> validator.isValidOwner(user2, item));
    }

    @Test
    void isAvailableTest() throws Exception {
        assertThrows(InvalidParameterException.class, () -> validator.isAvailable(item));
    }

    @Test
    void isValidItemTest() throws Exception {
        Optional<Item> itemOptional = Optional.ofNullable(item);
        Item item = validator.isValidItem(itemOptional);
        JsonContent<Item> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());

        Optional<Item> itemOptional2 = Optional.ofNullable(null);
        assertThrows(NotFoundException.class, () -> validator.isValidItem(itemOptional2));
    }

    @Test
    void isValidTextTest() throws Exception {
        assertThrows(InvalidParameterException.class, () -> validator.isValidText(null));
        assertThrows(InvalidParameterException.class, () -> validator.isValidText(""));
        assertThrows(InvalidParameterException.class, () -> validator.isValidText(" "));
    }

}
