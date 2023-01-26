package ru.practicum.shareit.user.validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.security.InvalidParameterException;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JsonTest
public class UserValidatorJsonTest {
    @Autowired
    private JacksonTester<User> json;
    UserValidator validator = new UserValidator();

    @Test
    void isValidUserTest() throws Exception {
        Optional<User> userOptional = Optional.ofNullable(new User(1L, "User 1", "1@ya.ru"));
        User user = validator.isValidUser(userOptional);

        JsonContent<User> result = json.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("User 1");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("1@ya.ru");
    }

    @Test
    void userNotFoundExceptionTest() throws Exception {
        Optional<User> userOptional = Optional.ofNullable(null);

        assertThrows(NotFoundException.class, () -> validator.isValidUser(userOptional));
    }

    @Test
    void isValidEmailTest() throws Exception {
        UserDto userDto1 = new UserDto(1L, "User 1", null);
        assertThrows(InvalidParameterException.class, () -> validator.isValidEmail(userDto1));

        UserDto userDto2 = new UserDto(1L, "User 1", "");
        assertThrows(InvalidParameterException.class, () -> validator.isValidEmail(userDto2));

        UserDto userDto3 = new UserDto(1L, "User 1", " ");
        assertThrows(InvalidParameterException.class, () -> validator.isValidEmail(userDto3));

        UserDto userDto4 = new UserDto(1L, "User 1", "1ya.ru");
        assertThrows(InvalidParameterException.class, () -> validator.isValidEmail(userDto4));

    }

}
