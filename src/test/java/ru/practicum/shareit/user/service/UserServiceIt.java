package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.security.InvalidParameterException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class UserServiceIt {
    @Autowired
    UserService userService;
    UserDto userDto1;

    @BeforeEach
    void before() {
        userDto1 = userService.saveNewUser(new UserDto(1L, "User 1", "1@ya.ru"));
    }

    @AfterEach
    void after() {
        List<UserDto> list = userService.getAllUsers();
        for(UserDto u : list) {
            userService.deleteUserById(u.getId());
        }
    }

    @Test
    void saveNewUserTest() {
        log.info("Запущен тест №1 saveNewUserTest()");

        assertThat(userDto1.equals(userService.getUserDtoById(userDto1.getId())));

        List<UserDto> list = userService.getAllUsers();
        assertNotNull(list, "Список не возвращается.");
        assertEquals(1, list.size(), "Неверное количество элементов.");
    }

    @Test
    void saveNewNotValidUserTest() {
        log.info("Запущен тест №2 saveNewNotValidUserTest()");
        assertThrows(DataIntegrityViolationException.class, () -> userService.saveNewUser(
                new UserDto(2L, "User 2", "1@ya.ru")));

        assertThrows(InvalidParameterException.class, () -> userService.saveNewUser(
                new UserDto(null, "User 1", null)));

        assertThrows(InvalidParameterException.class, () -> userService.saveNewUser(
                new UserDto(null, "User 1", "hjddghdsbgjh")));

        assertThrows(InvalidParameterException.class, () -> userService.saveNewUser(
                new UserDto(null, "User 1", " ")));
    }

    @Test
    void updateUserTest() {
        log.info("Запущен тест №3 updateUserTest()");

        userDto1.setName("User 1 new");
        userService.updateUser(userDto1.getId(), userDto1);

        UserDto userDto2 = userService.getUserDtoById(userDto1.getId());
        List<UserDto> list = userService.getAllUsers();

        assertThat(userDto1.equals(userDto2));
        assertNotNull(list, "Список не возвращается.");
        assertEquals(1, list.size(), "Неверное количество элементов'.");
    }

    @Test
    void updateNotValidUserTest() {
        log.info("Запущен тест №4 updateNotValidUserTest()");

        assertThrows(NotFoundException.class, () -> userService.updateUser(111L,
                new UserDto(111L, "User 1", "3@ya.ru")));

        UserDto userDto2 = userService.updateUser(userDto1.getId(), new UserDto(userDto1.getId(), "User 1", "njkdhinb"));
        assertThat(userDto1.equals(userDto2));
    }

    @Test
    void deleteUserByIdTest() {
        log.info("Запущен тест №5 deleteUserByIdTest()");
        userService.deleteUserById(userDto1.getId());

        List<UserDto> list = userService.getAllUsers();
        assertNotNull(list, "Список не возвращается.");
        assertEquals(0, list.size(), "Неверное количество элементов'.");

        assertThrows(NotFoundException.class, () -> userService.getUserDtoById(userDto1.getId()));
    }

    @Test
    void getUserDtoByIdTest() {
        log.info("Запущен тест №6 getUserDtoByIdTest()");

        UserDto userDto2 = userService.getUserDtoById(userDto1.getId());
        assertThat(userDto1.equals(userDto2));
    }

    @Test
    void getAllUsersTest() {
        log.info("Запущен тест №7 getAllUsersTest()");
        UserDto userDto2 = userService.saveNewUser(new UserDto(77L, "User 2", "77@ya.ru"));

        List<UserDto> list = userService.getAllUsers();

        assertNotNull(list, "Список не возвращается.");
        assertEquals(2, list.size(), "Неверное количество элементов.");
        assertThat(list.get(0).equals(userDto1));
        assertThat(list.get(1).equals(userDto2));
    }

}
