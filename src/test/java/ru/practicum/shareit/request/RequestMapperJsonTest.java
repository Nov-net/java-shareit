package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoShot;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestMapperJsonTest {
    @Autowired
    private JacksonTester<RequestDto> json;

    @Autowired
    private JacksonTester<Request> json2;

    List<ItemDtoForRequest> itemsDto = new ArrayList<>();
    LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
    List<Item> items = new ArrayList<>();
    User requestor = new User(1L, "User 1", "1@ya.ru");
    RequestDtoShot requestDtoShotTest = new RequestDtoShot(1L, "Описание", now);
    Request requestTest = new Request(1L, "Описание", items, requestor, now);

    @Test
    void toRequestDtoTest() throws Exception {
        RequestDto requestDto = RequestMapper.toRequestDto(requestTest);
        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestTest.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(requestTest.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(itemsDto);
    }

    @Test
    void mapToRequestDtoTest() throws Exception {
        List<RequestDto> requestDto = RequestMapper.mapToRequestDto(List.of(requestTest));
        JsonContent<RequestDto> result = json.write(requestDto.get(0));

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestTest.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(requestTest.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(itemsDto);
    }

    @Test
    void toRequestTest() throws Exception {
        Request request = RequestMapper.toRequest(requestDtoShotTest, requestor);
        JsonContent<Request> result = json2.write(request);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestTest.getDescription());
    }

    @Test
    void requestBilderTest() throws Exception {
        Request request = RequestMapper.requestBilder(requestTest);
        JsonContent<Request> result = json2.write(request);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestTest.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(requestTest.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(itemsDto);
    }

}
