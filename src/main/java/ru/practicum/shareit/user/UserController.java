package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserService userService;

    /*GET /users - получение списка пользователей*/
    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers();
    }

    /*POST /users - создание нового пользователя*/
    @PostMapping
    public UserDto saveNewUser(@RequestBody UserDto userDto) {
        return userService.saveNewUser(userDto);
    }

    /*PATCH /users/{userId} - обновление пользователя*/
    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        return userService.updateUser(userId, userDto);
    }

    /*GET /users/{userId} - получение пользователя по userId*/
    @GetMapping("/{userId}")
    public UserDto getUserDtoById(@PathVariable(required = false) long userId) {
        return userService.getUserDtoById(userId);
    }

    /*DELETE /users/{userId} - удаление пользователя по userId*/
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable long userId) {
        userService.deleteUserById(userId);
    }

}
