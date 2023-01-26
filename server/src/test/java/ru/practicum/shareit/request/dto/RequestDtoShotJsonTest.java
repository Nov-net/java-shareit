package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestDtoShotJsonTest {
    @Autowired
    JacksonTester<RequestDtoShot> json;

    LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
    String nowString = now.toString();

    @Test
    void testRequestDtoShot() throws Exception {
        RequestDtoShot requestDto = new RequestDtoShot(1L, "Описание", now);
        JsonContent<RequestDtoShot> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestDto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(requestDto.getCreated().toString());
    }

    @Test
    void testRequestDtoShotNoArgs() throws Exception {
        RequestDtoShot requestDto = new RequestDtoShot();
        JsonContent<RequestDtoShot> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(null);
    }

    @Test
    void testRequestDtoSetArgs() throws Exception {
        RequestDtoShot requestDto = new RequestDtoShot();
        requestDto.setRequestorId(1L);
        requestDto.setDescription("Описание");
        requestDto.setCreated(now);

        JsonContent<RequestDtoShot> result2 = json.write(requestDto);

        assertThat(result2).extractingJsonPathNumberValue("$.requestorId").isEqualTo(1);
        assertThat(result2).extractingJsonPathStringValue("$.description").isEqualTo("Описание");
        assertThat(result2).extractingJsonPathValue("$.created").isEqualTo(nowString);
    }

    @Test
    void testRequestDtoBilder() throws Exception {
        RequestDtoShot requestDto = RequestDtoShot.builder()
                .requestorId(1L)
                .description("Описание")
                .created(now)
                .build();

        JsonContent<RequestDtoShot> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(requestDto.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(requestDto.getCreated().toString());
    }

}
