package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    JacksonTester<ItemDto> json;

    List<Item> items = new ArrayList<>();
    LocalDateTime now = LocalDateTime.of(2023, 02, 15, 21, 07, 57);
    User user = new User(1L, "User 1", "1@ya.ru");
    Request request = new Request(1L, "Описание", items, user, now);
    Item item = new Item(1L, "Вещь", "Описание", true, user, request);
    BookingDtoForItemDto booking = new BookingDtoForItemDto(1L, now, now, item, 1L, Status.APPROVED);
    CommentDto comment = new CommentDto(1L, "Комментарий", "Имя автора", now);


    @Test
    void testItem() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Вещь", "Описание", true, booking, booking, List.of(comment), 1L);
        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathValue("$.lastBooking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.nextBooking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.comments").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void testItemNoArgs() throws Exception {
        ItemDto itemDto = new ItemDto();
        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isNull();
        assertThat(result).extractingJsonPathStringValue("$.name").isNull();
        assertThat(result).extractingJsonPathStringValue("$.description").isNull();
        assertThat(result).extractingJsonPathBooleanValue("$.available").isNull();
        assertThat(result).extractingJsonPathValue("$.owner").isNull();
        assertThat(result).extractingJsonPathValue("$.request").isNull();
    }

    @Test
    void testItemSetArgs() throws Exception {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Вещь");
        itemDto.setDescription("Описание");
        itemDto.setAvailable(true);
        itemDto.setLastBooking(booking);
        itemDto.setNextBooking(booking);
        itemDto.setComments(List.of(comment));
        itemDto.setRequestId(1L);

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathValue("$.lastBooking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.nextBooking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.comments").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void testItemBilder() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Вещь")
                .description("Описание")
                .available(true)
                .lastBooking(booking)
                .nextBooking(booking)
                .comments(List.of(comment))
                .requestId(1L)
                .build();

        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathValue("$.lastBooking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.nextBooking").isNotNull();
        assertThat(result).extractingJsonPathValue("$.comments").isNotNull();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

}
