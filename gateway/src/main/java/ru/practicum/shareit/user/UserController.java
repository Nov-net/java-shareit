package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    /*GET /users - получение списка пользователей*/
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    /*POST /users - создание нового пользователя*/
    @PostMapping
    public ResponseEntity<Object> saveNewUser(@RequestBody UserDto userDto) {
        return userClient.saveNewUser(userDto);
    }

    /*PATCH /users/{userId} - обновление пользователя*/
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable long userId, @RequestBody UserDto userDto) {
        return userClient.updateUser(userId, userDto);
    }

    /*GET /users/{userId} - получение пользователя по userId*/
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserDtoById(@PathVariable(required = false) long userId) {
        return userClient.getUserDtoById(userId);
    }

    /*DELETE /users/{userId} - удаление пользователя по userId*/
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable long userId) {
        return userClient.deleteUserById(userId);
    }

}
