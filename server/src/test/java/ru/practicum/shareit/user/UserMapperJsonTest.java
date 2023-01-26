package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserMapperJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Autowired
    private JacksonTester<User> json2;

    UserDto userDtoTest = new UserDto(1L, "User 1", "1@ya.ru");
    User userTest = new User(1L, "User 1", "1@ya.ru");

    @Test
    void toUserDtoTest() throws Exception {
        UserDto userDto = UserMapper.toUserDto(userTest);
        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userTest.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userTest.getEmail());
    }

    @Test
    void mapToUserDtoTest() throws Exception {
        List<UserDto> userDto = UserMapper.mapToUserDto(List.of(userTest));
        JsonContent<UserDto> result = json.write(userDto.get(0));

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userTest.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userTest.getEmail());
    }

    @Test
    void toUserTest() throws Exception {
        User user = UserMapper.toUser(userDtoTest);
        JsonContent<User> result = json2.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userTest.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userTest.getEmail());
    }

    @Test
    void requestBilderTest() throws Exception {
        User user = UserMapper.userBilder(userTest);
        JsonContent<User> result = json2.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userTest.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(userTest.getEmail());
    }

}
