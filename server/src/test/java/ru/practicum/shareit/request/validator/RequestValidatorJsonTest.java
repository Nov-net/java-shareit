package ru.practicum.shareit.request.validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
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
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
public class RequestValidatorJsonTest {
    @Autowired
    private JacksonTester<Request> json;
    RequestValidator validator = new RequestValidator();

    @Test
    void isValidDescriptionTest() throws Exception {
        RequestDtoShot requestDto1 = new RequestDtoShot(1L, null, null);
        assertThrows(InvalidParameterException.class, () -> validator.isValidDescription(requestDto1));

        RequestDtoShot requestDto2 = new RequestDtoShot(1L, "", null);
        assertThrows(InvalidParameterException.class, () -> validator.isValidDescription(requestDto2));

        RequestDtoShot requestDto3 = new RequestDtoShot(1L, " ", null);
        assertThrows(InvalidParameterException.class, () -> validator.isValidDescription(requestDto3));
    }

    @Test
    void isValidRequestTest() throws Exception {
        List<Item> items = new ArrayList<>();
        LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
        User requestor = new User(1L, "User 1", "1@ya.ru");

        Optional<Request> requestOptional = Optional.ofNullable(new Request(1L, "Описание", items,
                requestor, now));

        Request request = validator.isValidRequest(requestOptional);

        JsonContent<Request> result = json.write(request);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(request.getDescription());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(request.getCreated().toString());
        assertThat(result).extractingJsonPathArrayValue("$.items").isEqualTo(request.getItems());
    }

    @Test
    void userValidatorNotFoundExceptionTest() {
        Optional<Request> requestOptional = Optional.ofNullable(null);
        Request request = validator.isValidRequest(requestOptional);

        assertNull(request, "Вернулся непустой request");
    }

    @Test
    void isValidParametersTest() throws Exception {
        assertThrows(InvalidParameterException.class, () -> validator.isValidParameters(-1, 1));
        assertThrows(InvalidParameterException.class, () -> validator.isValidParameters(1, -1));

    }

}
