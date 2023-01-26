package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@JsonTest
public class ItemMapperJsonTest {
    @Autowired
    JacksonTester<ItemDto> json;
    @Autowired
    JacksonTester<ItemDtoForRequest> json2;
    @Autowired
    JacksonTester<Item> json3;

    LocalDateTime start = LocalDateTime.of(2023, 02, 15, 21, 07, 57);
    LocalDateTime end = LocalDateTime.of(2023, 03, 15, 21, 07, 57);
    User user = new User(1L, "User 1", "1@ya.ru");
    Request request = new Request(1L, "Описание", null, user, start);
    Item item = new Item(1L, "Вещь", "Описание", true, user, request);
    BookingDtoForItemDto booking = new BookingDtoForItemDto(1L, start, end, item, 1L, Status.APPROVED);
    ItemDto itemDto = new ItemDto(1L, "Вещь", "Описание", true, booking, booking, null, 1L);

    @Test
    void toItemDtoTest() throws Exception {
        ItemDto itemDto = ItemMapper.toItemDto(item);
        JsonContent<ItemDto> result = json.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void mapToItemDtoTest() throws Exception {
        List<ItemDto> itemDto = ItemMapper.mapToItemDto(List.of(item));
        JsonContent<ItemDto> result = json.write(itemDto.get(0));

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);

        List<ItemDto> itemDto2 = ItemMapper.mapToItemDto(null);
        assertNull(itemDto2, "Вернулся непустой request");
    }

    @Test
    void toItemDtoForRequestTest() throws Exception {
        ItemDtoForRequest itemDto = ItemMapper.toItemDtoForRequest(item);
        JsonContent<ItemDtoForRequest> result = json2.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void mapToItemDtoForRequestTest() throws Exception {
        List<ItemDtoForRequest> itemDto = ItemMapper.mapToItemDtoForRequest(List.of(item));
        JsonContent<ItemDtoForRequest> result = json2.write(itemDto.get(0));

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);

        List<ItemDtoForRequest> itemDto2 = ItemMapper.mapToItemDtoForRequest(null);
        assertNull(itemDto2, "Вернулся непустой request");
    }

    @Test
    void toItemTest() throws Exception {
        Item item = ItemMapper.toItem(itemDto);
        JsonContent<Item> result = json3.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
    }

    @Test
    void itemBilderTest() throws Exception {
        Item item2 = ItemMapper.itemBilder(item);
        JsonContent<Item> result = json3.write(item2);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathValue("$.owner").isNotNull();
    }

}
