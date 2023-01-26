package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.security.InvalidParameterException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class ItemServiceIt {
    @Autowired
    ItemService service;

    @Autowired
    UserService userService;


    @Test
    void searchItemsTest() {
        UserDto user = userService.saveNewUser(new UserDto(1L, "User 1", "1@ya.ru"));
        ItemDto item = service.saveNewItem(user.getId(), new ItemDto(1L, "Вещь", "Описание", false, null, null, null, null));

        List<ItemDto> items = service.searchItems("ещь", 1, 10);
        assertNotNull(items, "Список не возвращается.");

        List<ItemDto> items2 = service.searchItems("", 1, 10);
        assertEquals(items2.size(), 0, "Размер списка не совпадает");
    }

    @Test
    void getItemsDtoByUserIdTest() {
        UserDto user = userService.saveNewUser(new UserDto(1L, "User 1", "1@ya.ru"));
        ItemDto item = service.saveNewItem(user.getId(), new ItemDto(1L, "Вещь", "Описание", false, null, null, null, null));

        List<ItemDto> items = service.getItemsDtoByUserId(user.getId(), 1, 10);
        assertNotNull(items, "Список не возвращается.");
        assertEquals(items.size(), 1, "Размер списка не совпадает");
    }

}
