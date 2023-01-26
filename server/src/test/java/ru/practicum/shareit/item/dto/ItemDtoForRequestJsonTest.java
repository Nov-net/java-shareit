package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class ItemDtoForRequestJsonTest {
    @Autowired
    JacksonTester<ItemDtoForRequest> json;

    @Test
    void testItem() throws Exception {
        ItemDtoForRequest item = new ItemDtoForRequest(1L, "Вещь", "Описание", true, 1L);
        JsonContent<ItemDtoForRequest> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void testItemNoArgs() throws Exception {
        ItemDtoForRequest item = new ItemDtoForRequest();
        JsonContent<ItemDtoForRequest> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isNull();
        assertThat(result).extractingJsonPathStringValue("$.name").isNull();
        assertThat(result).extractingJsonPathStringValue("$.description").isNull();
        assertThat(result).extractingJsonPathBooleanValue("$.available").isNull();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isNull();
    }

    @Test
    void testItemSetArgs() throws Exception {
        ItemDtoForRequest item = new ItemDtoForRequest();
        item.setId(1L);
        item.setName("Вещь");
        item.setDescription("Описание");
        item.setAvailable(true);
        item.setRequestId(1L);

        JsonContent<ItemDtoForRequest> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

    @Test
    void testItemBilder() throws Exception {
        ItemDtoForRequest item = ItemDtoForRequest.builder()
                .id(1L)
                .name("Вещь")
                .description("Описание")
                .available(true)
                .requestId(1L)
                .build();

        JsonContent<ItemDtoForRequest> result = json.write(item);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(item.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    }

}
