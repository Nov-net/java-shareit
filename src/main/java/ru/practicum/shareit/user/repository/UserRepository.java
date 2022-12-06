package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<UserDto> getAllUsers();
    UserDto saveNewUser(UserDto userDto);
    UserDto updateUser(long userId, UserDto userDto);
    UserDto getUserDtoById(long userId);
    User findUserById(long id);
    void deleteUserById(long userId);
}
