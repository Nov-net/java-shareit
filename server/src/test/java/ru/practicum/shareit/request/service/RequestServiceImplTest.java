package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoShot;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {
    @Mock
    RequestRepository repository;
    @Mock
    UserRepository userRepository;
    RequestService service;

    List<Item> items = new ArrayList<>();
    List<ItemDtoForRequest> itemsDto = new ArrayList<>();
    LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
    User requestor;
    Request request;
    RequestDto requestDto;
    RequestDtoShot requestDtoShot;

    @BeforeEach
    void before() {
        service = new RequestServiceImpl(repository, userRepository);
        requestor = new User(1L, "User 1", "1@ya.ru");
        request = new Request(1L, "Описание", items, requestor, now);
        requestDto = new RequestDto(1L, "Описание", now, itemsDto);
        requestDtoShot = new RequestDtoShot(1L, "Описание", now);
    }

    @Test
    void saveNewRequestTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(requestor));
        Mockito
                .when(repository.save(any()))
                .thenReturn(request);

        assertEquals(service.saveNewRequest(requestor.getId(), requestDtoShot).getId(), 1L);
    }

    @Test
    void getRequestDtoByUserIdTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(requestor));
        Mockito
                .when(repository.findAllByRequestorIdIsOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(request));

        assertEquals(service.getRequestDtoByUserId(requestor.getId()).size(), 1);
    }

    @Test
    void getRequestDtoByRequestIdTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(requestor));

        Mockito
                .when(repository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(request));

        assertEquals(service.getRequestDtoByRequestId(requestor.getId(), 1L), requestDto);
    }

    @Test
    void getRequestDtoByRequestIdThrowTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(requestor));

        Mockito
                .when(repository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class, () -> service.getRequestDtoByRequestId(requestor.getId(), 100L));
    }

    @Test
    void getAllRequestTest() {
        Mockito
                .when(userRepository.findById(any()))
                .thenReturn(Optional.ofNullable(requestor));

        Pageable pageable = PageRequest.of(1,1);
        Mockito
                .when(repository.findAllIsOrderByCreatedDesc(pageable))
                .thenReturn(null);

        assertNull(service.getAllRequest(requestor.getId(), 1, 1));
    }

}
