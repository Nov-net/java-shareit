package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestDtoJsonTest {
    @Autowired
    JacksonTester<RequestDto> json;

    List<ItemDtoForRequest> items = new ArrayList<>();
    LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);

    @Test
    void testRequestDto() throws Exception {
        RequestDto requestDto = new RequestDto(1L, "Описание", now, items);
        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestDto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(requestDto.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(requestDto.getItems());
    }

    @Test
    void testRequestDtoNoArgs() throws Exception {
        RequestDto requestDto = new RequestDto();
        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.items").isEqualTo(null);
    }

    @Test
    void testRequestDtoSetArgs() throws Exception {
        RequestDto requestDto = new RequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Описание");
        requestDto.setCreated(now);
        requestDto.setItems(items);

        JsonContent<RequestDto> result2 = json.write(requestDto);

        assertThat(result2).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result2).extractingJsonPathStringValue("$.description").isEqualTo("Описание");
        assertThat(result2).extractingJsonPathValue("$.created").isEqualTo(requestDto.getCreated().toString());
        assertThat(result2).extractingJsonPathArrayValue("$.items").isEqualTo(items);
    }

    @Test
    void testRequestDtoBilder() throws Exception {
        RequestDto requestDto = RequestDto.builder()
                .id(1L)
                .description("Описание")
                .created(now)
                .items(items)
                .build();

        JsonContent<RequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestDto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(requestDto.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(requestDto.getItems());
    }

}
