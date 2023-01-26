package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    ItemRepository repository;
    @Mock
    UserRepository userRepository;
    @Mock
    BookingRepository bookingRepository;
    @Mock
    RequestRepository requestRepository;
    @Mock
    CommentRepository commentRepository;
    ItemService service;


    List<Item> items;
    LocalDateTime now;
    LocalDateTime start;
    LocalDateTime end;
    User user;
    User user2;
    Request request;
    Item item;
    BookingDtoForItemDto booking;
    Booking bookingB;
    CommentDto comment;
    Comment commentC;
    List<Comment> comments;
    ItemDto itemDto;


    @BeforeEach
    void before() {
        service = new ItemServiceImpl(repository, userRepository, bookingRepository, requestRepository, commentRepository);

        items = new ArrayList<>();
        now = LocalDateTime.of(2023, 02, 15, 21, 07, 57);
        start = LocalDateTime.of(2022, 12, 01, 21, 07, 57);
        end = LocalDateTime.of(2022, 12, 02, 21, 07, 57);
        user = new User(1L, "User 1", "1@ya.ru");
        user2 = new User(2L, "User 2", "2@ya.ru");
        request = new Request(1L, "Описание", items, user, now);
        item = new Item(1L, "Вещь", "Описание", false, user, request);
        booking = new BookingDtoForItemDto(1L, now, now, item, 1L, Status.APPROVED);
        bookingB = new Booking(1L, start, end, item, user2, Status.APPROVED);
        comment = new CommentDto(1L, "Комментарий", "Имя автора", now);
        commentC = new Comment(1L, "Комментарий", item, user, now);
        itemDto = new ItemDto(1L, "Вещь", "Описание", false, booking, booking, List.of(comment), 1L);
    }

    @Test
    void saveNewItemTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(request));
        Mockito
                .when(repository.save(any()))
                .thenReturn(item);

        assertEquals(service.saveNewItem(user.getId(), itemDto).getId(), 1L);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class, () -> service.saveNewItem(user2.getId(), itemDto));
    }

    @Test
    void updateItemTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(requestRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(request));
        Mockito
                .when(repository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(repository.save(any()))
                .thenReturn(item);

        assertEquals(service.updateItem(user.getId(), itemDto.getId(), itemDto).getId(), 1L);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(null));

        assertThrows(NotFoundException.class, () -> service.updateItem(user2.getId(), itemDto.getId(), itemDto));
    }

    @Test
    void getItemDtoByItemIdTest() {
        Mockito
                .when(commentRepository.findAllByItemIdIsOrderById(anyLong()))
                .thenReturn(null);
        Mockito
                .when(repository.getById(anyLong()))
                .thenReturn(item);

        assertEquals(service.getItemDtoByItemId(user.getId(), itemDto.getId()).getId(), 1L);
    }

    @Test
    void addCommentTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(bookingRepository.findByBooker_IdAndOrderByStart_DateDesc(anyLong()))
                .thenReturn(List.of(bookingB));
        Mockito
                .when(repository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(commentRepository.save(any()))
                .thenReturn(commentC);

        assertEquals(service.addComment(user2.getId(), itemDto.getId(), comment).getText(), comment.getText());
    }

}
