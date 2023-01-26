package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShot;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.validator.BookingValidator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnknownState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.request.validator.RequestValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    /*POST /bookings - создание нового бронирования*/
    @Override
    public BookingDto saveNewBooking(long userId, BookingShot bookingShot) {
        log.info("Запрос на добавление нового бронирования {} от пользователя с id {}", bookingShot, userId);

        log.info("Получение пользователя по id {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        User user = UserValidator.isValidUser(userOptional);

        BookingValidator.isValidBooking(bookingShot);

        log.info("Получение вещи по id {}", bookingShot.getItemId());
        Optional<Item> itemOptional = itemRepository.findById(bookingShot.getItemId());
        Item item = ItemValidator.isValidItem(itemOptional);
        log.info("Нашли и проверили вещь: {}", item);

        ItemValidator.isAvailable(item);

        if (user.getId().equals(item.getOwner().getId())) {
            log.info("booker_id = owner_id");
            throw new NotFoundException("Бронирование не найдено");
        }

        Booking booking = BookingMapper.toBooking(bookingShot);

        booking.setStatus(Status.WAITING);
        booking.setBooker(user);
        booking.setItem(item);
        Booking newBooking = repository.save(booking);
        log.info("Добавлено новое бронирование {}", newBooking);

        return BookingMapper.toBookingDto(newBooking);
    }

    /*GET /bookings/{bookingId} - получение бронирования по id бронирования и по bookerId*/
    @Override
    public BookingDto getBookingDtoByBookingId(long userId, long bookingId) {
        log.info("Запрошено бронирование с id {} от пользователя с userId {}", bookingId, userId);

        Optional<Booking> bookingOptional = repository.findBookingById(bookingId);
        Booking booking = BookingValidator.isValidBooking(bookingOptional);
        log.info("id букера {}, id овнера {}", booking.getBooker().getId(), booking.getItem().getOwner().getId());

        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.toBookingDto(booking);
        } else {
            log.info("Бронирование другого пользователя");
            throw new NotFoundException("Бронирование не найдено");
        }
    }

    /*GET /bookings - получение бронирования по bookerId*/
    @Override
    public List<BookingDto> getAllBookingDtoByBookerId(long userId, String state, Integer from, Integer size) {
        RequestValidator.isValidParameters(from, size);

        Optional<User> userOptional = userRepository.findById(userId);
        UserValidator.isValidUser(userOptional);

        Pageable pageable = PageRequest.of(from / size, size);
        List<Booking> bookings = repository.findByBookerId(userId, pageable).toList();

        return sortListBooking(state, bookings);
    }

    /*GET /bookings/{owner} - получение списка всех бронирований по ownerId*/
    @Override
    public List<BookingDto> getBookingDtoByOwnerId(long userId, String state, Integer from, Integer size) {
        RequestValidator.isValidParameters(from, size);

        Optional<User> userOptional = userRepository.findById(userId);
        UserValidator.isValidUser(userOptional);

        Pageable pageable = PageRequest.of(from / size, size);
        List<Booking> bookings = repository.findByOwner_Id(userId, pageable).toList();

        return sortListBooking(state, bookings);
    }

    /*PATCH /bookings/:bookingId?approved=true - подтверждение бронирования*/
    @Override
    public BookingDto approveBooking(long userId, long bookingId, Boolean approved) {
        log.info("Запрос на подтверждение бронирования от userId {} для бронирования bookingId {}, " +
                "подтверждение получено {}", userId, bookingId, approved);

        Optional<User> userOptional = userRepository.findById(userId);
        User user = UserValidator.isValidUser(userOptional);

        Optional<Booking> bookingOptional = repository.findBookingById(bookingId);
        Booking booking = BookingValidator.isValidBooking(bookingOptional);

        if (booking.getStatus().equals(Status.APPROVED)) {
            log.info("Статус бронирования не может быть изменен после подтверждения");
            throw new InvalidParameterException("Статус бронирования не может быть изменен после подтверждения");
        }

        ItemValidator.isValidOwner(user, booking.getItem());

        if (Boolean.TRUE.equals(approved)) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        log.info("Статус бронирования обновлен на {}", booking.getStatus());

        Booking newBooking = repository.save(booking);
        log.info("Бронирование обновлено {}", newBooking);

        return BookingMapper.toBookingDto(newBooking);
    }

    private List<BookingDto> sortListBooking(String state, List<Booking> bookings) {

        switch (state) {
            case "ALL":
                return bookings.stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case "CURRENT":
                return bookings.stream()
                        .filter(i -> i.getStart().isBefore(LocalDateTime.now()))
                        .filter(i -> i.getEnd().isAfter(LocalDateTime.now()))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case "PAST":
                return bookings.stream()
                        .filter(i -> i.getEnd().isBefore(LocalDateTime.now()))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case "FUTURE":
                return bookings.stream()
                        .filter(i -> i.getStart().isAfter(LocalDateTime.now()))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case "WAITING":
                return bookings.stream()
                        .filter(i -> i.getStatus().equals(Status.WAITING))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            case "REJECTED":
                return bookings.stream()
                        .filter(i -> i.getStatus().equals(Status.REJECTED))
                        .map(BookingMapper::toBookingDto)
                        .collect(Collectors.toList());

            default:
                log.info("Невалидный параметр state {}", state);
                throw new UnknownState("Unknown state: " + state);
        }
    }

}
