package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository repository;

    /*GET /users - получение списка пользователей*/
    @Override
    public List<UserDto> getAllUsers() {
        return repository.getAllUsers();
    }

    /*POST /users - создание нового пользователя*/
    @Override
    public UserDto saveNewUser(UserDto userDto) {
        return repository.saveNewUser(userDto);
    }

    /*PATCH /users/{userId} - обновление пользователя*/
    @Override
    public UserDto updateUser(long userId, UserDto userDto) {
        return repository.updateUser(userId, userDto);
    }

    /*GET /users/{userId} - получение пользователя по userId*/
    @Override
    public UserDto getUserDtoById(long userId) {
        return repository.getUserDtoById(userId);
    }

    /*DELETE /users/{userId} - удаление пользователя по userId*/
    @Override
    public void deleteUserById(long userId) {
        repository.deleteUserById(userId);
    }

}
