package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class ItemJsonTest {
    @Autowired
    JacksonTester<Item> json;

    List<Item> items = new ArrayList<>();
    LocalDateTime start = LocalDateTime.of(2023, 02, 15, 21, 07, 57);
    User user = new User(1L, "User 1", "1@ya.ru");
    Request request = new Request(1L, "Описание", items, user, start);

    @Test
    void testItem() throws Exception {
        Item item = new Item(1L, "Вещь", "Описание", true, user, request);
        JsonContent<Item> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathValue("$.owner").isNotNull();
        assertEquals(item.getRequest(), request, "Объекты не совпадают");
    }

    @Test
    void testItemNoArgs() throws Exception {
        Item item = new Item();
        JsonContent<Item> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isNull();
        assertThat(result).extractingJsonPathStringValue("$.name").isNull();
        assertThat(result).extractingJsonPathStringValue("$.description").isNull();
        assertThat(result).extractingJsonPathBooleanValue("$.available").isNull();
        assertThat(result).extractingJsonPathValue("$.owner").isNull();
        assertThat(result).extractingJsonPathValue("$.request").isNull();
    }

    @Test
    void testItemSetArgs() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Вещь");
        item.setDescription("Описание");
        item.setOwner(user);
        item.setRequest(request);

        JsonContent<Item> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathValue("$.owner").isNotNull();
        assertEquals(item.getRequest(), request, "Объекты не совпадают");
    }

    @Test
    void testItemBilder() throws Exception {
        Item item = Item.builder()
                .id(1L)
                .name("Вещь")
                .description("Описание")
                .owner(user)
                .request(request)
                .build();

        JsonContent<Item> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathValue("$.owner").isNotNull();
        assertEquals(item.getRequest(), request, "Объекты не совпадают");
    }

}
