package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.dto.BookingShot;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnknownState;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoShot;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.request.service.RequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    ItemRepository itemRepository;
    @Mock
    BookingRepository repository;
    BookingService service;

    List<Item> items;
    LocalDateTime start = LocalDateTime.of(2023, 02, 15, 21, 07, 57);
    LocalDateTime end = LocalDateTime.of(2023, 03, 15, 21, 07, 57);
    User user;
    User user2;
    Request request;
    Item item;
    BookingShot bookingShot;
    BookingDto bookingDto;
    BookingDtoForItemDto bookingDtoForItem;
    Booking booking;

    @BeforeEach
    void before() {
        service = new BookingServiceImpl(repository, userRepository, itemRepository);
        user = new User(1L, "User 1", "1@ya.ru");
        user2 = new User(2L, "User 2", "2@ya.ru");
        items = new ArrayList<>();
        request = new Request(1L, "Описание", items, user, start);
        item = new Item(1L, "Вещь", "Описание", true, user2, request);

        bookingShot = new BookingShot(1L, start, end);
        bookingDto = new BookingDto(1L, start, end, item, user, Status.WAITING);
        bookingDtoForItem = new BookingDtoForItemDto(1L, start, end, item, 1L, Status.WAITING);
        booking = new Booking(1L, start, end, item, user, Status.WAITING);

    }

    @Test
    void saveNewBookingTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        Mockito
                .when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(item));
        Mockito
                .when(repository.save(any()))
                .thenReturn(booking);

        assertEquals(service.saveNewBooking(user.getId(), bookingShot).getId(), 1L);

        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));

        assertThrows(NotFoundException.class, () -> service.saveNewBooking(user2.getId(), bookingShot));
    }

    @Test
    void getBookingDtoByBookingIdTest() {
        Mockito
                .when(repository.findBookingById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));

        assertEquals(service.getBookingDtoByBookingId(user.getId(), booking.getId()).getStart(), bookingDto.getStart());
        assertEquals(service.getBookingDtoByBookingId(user.getId(), booking.getId()).getEnd(), bookingDto.getEnd());
        assertThrows(NotFoundException.class, () -> service.getBookingDtoByBookingId(3L, 1L));
    }

    @Test
    void getAllBookingDtoByBookerIdTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito
                .when(repository.findByBookerId(anyLong(), any()))
                .thenReturn(null);

        assertThrows(NullPointerException.class, () -> service.getAllBookingDtoByBookerId(user.getId(), "ALL", 1, 1));
    }

    @Test
    void getBookingDtoByOwnerIdTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));

        Mockito
                .when(repository.findByOwner_Id(anyLong(), any()))
                .thenReturn(null);

        assertThrows(NullPointerException.class, () -> service.getBookingDtoByOwnerId(user.getId(), "ALL", 1, 1));
    }

    @Test
    void approveBookingTest() {
        Mockito
                .when(userRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user2));
        Mockito
                .when(repository.findBookingById(anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        Mockito
                .when(repository.save(any()))
                .thenReturn(booking);

        assertEquals(service.approveBooking(user2.getId(), bookingDto.getId(),false).getStatus(), Status.REJECTED);
        assertEquals(service.approveBooking(user2.getId(), bookingDto.getId(),true).getStatus(), Status.APPROVED);
        assertThrows(InvalidParameterException.class, () -> service.approveBooking(user2.getId(), bookingDto.getId(), true));
    }

}
