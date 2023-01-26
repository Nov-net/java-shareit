package ru.practicum.shareit.user.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserJsonTest {
    @Autowired
    JacksonTester<User> json;

    @Test
    void testUser() throws Exception {
        User user = new User(1L, "User 1", "1@ya.ru");

        JsonContent<User> result = json.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("User 1");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("1@ya.ru");
    }

    @Test
    void testUserSetArgs() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("User 1");
        user.setEmail("1@ya.ru");

        JsonContent<User> result = json.write(user);
        user.toString();

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(user.getEmail());
    }

    @Test
    void testUserBilder() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("User 1")
                .email("1@ya.ru")
                .build();

        JsonContent<User> result = json.write(user);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(user.getEmail());
    }

}
