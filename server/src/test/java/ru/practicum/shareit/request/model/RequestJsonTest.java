package ru.practicum.shareit.request.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestJsonTest {
    @Autowired
    JacksonTester<Request> json;

    List<Item> items = new ArrayList<>();
    LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
    User requestor = new User(1L, "User 1", "1@ya.ru");

    @Test
    void testRequest() throws Exception {
        Request request = new Request(1L, "Описание", items, requestor, now);
        JsonContent<Request> result = json.write(request);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(request.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(request.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(request.getItems());
    }

    @Test
    void testRequestNoArgs() throws Exception {
        Request request = new Request();
        JsonContent<Request> result = json.write(request);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.items").isEqualTo(null);
    }

    @Test
    void testRequestSetArgs() throws Exception {
        Request request = new Request();
        request.setId(1L);
        request.setDescription("Описание");
        request.setCreated(now);
        request.setItems(items);
        request.setRequestor(requestor);

        JsonContent<Request> result = json.write(request);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(request.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(request.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(request.getItems());
    }

    @Test
    void testRequestBilder() throws Exception {
        Request request = Request.builder()
                .id(1L)
                .description("Описание")
                .created(now)
                .items(items)
                .requestor(requestor)
                .build();

        JsonContent<Request> result = json.write(request);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(request.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(request.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(request.getItems());
    }

}
