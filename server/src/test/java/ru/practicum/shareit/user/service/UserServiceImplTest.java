package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository repository;
    UserService service;
    User user;
    UserDto userDto;

    @BeforeEach
    void before() {
        service = new UserServiceImpl(repository);
        user = new User(1L, "User 1", "1@ya.ru");
        userDto = new UserDto(1L, "User 1", "1@ya.ru");
    }

    @Test
    void saveNewUserTest() {
        Mockito
                .when(repository.save(any()))
                .thenReturn(user);

        assertThat(service.saveNewUser(userDto).getId().equals(1L));
    }

    @Test
    void saveNewNotValidUserTest() {
        Mockito
                .when(repository.save(any()))
                .thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> service.saveNewUser(userDto));
    }

    @Test
    void updateUserTest() {
        Mockito
                .when(repository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito
                .when(repository.save(any()))
                .thenReturn(user);

        assertThat(service.updateUser(1L, userDto).equals(user));
    }

    @Test
    void findAllUsersTest() {
        Mockito
                .when(repository.findAll())
                .thenReturn(List.of(user));

        assertThat(service.getAllUsers().size() == 1);
    }

    @Test
    void getUserByIdTest() {
        Mockito
                .when(repository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        assertThat(service.getUserDtoById(1L).getId().equals(1L));
    }


}
