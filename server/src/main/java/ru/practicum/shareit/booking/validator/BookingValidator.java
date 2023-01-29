package ru.practicum.shareit.booking.validator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingShot;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotFoundException;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
public class BookingValidator {

    static LocalDateTime now = LocalDateTime.now();

    @SneakyThrows
    public static void isValidBooking(BookingShot bookingShot) {
        isValidItemId(bookingShot);
        isValidStart(bookingShot);
        isValidEnd(bookingShot);

    }

    @SneakyThrows
    public static void isValidItemId(BookingShot bookingShot) {
        if (bookingShot.getItemId() == null) {
            log.info("Отсутствует itemId");
            throw new InvalidParameterException("Отсутствует itemId");
        }
    }

    @SneakyThrows
    public static void isValidEnd(BookingShot bookingShot) {
        if (bookingShot.getEnd() == null) {
            log.info("Отсутствует дата окончания бронирования");
            throw new InvalidParameterException("Отсутствует дата окончания бронирования");
        }

        if (bookingShot.getEnd().isBefore(now)) {
            log.info("Дата окончания бронирования не может быть в прошлом");
            throw new InvalidParameterException("Дата окончания бронирования не может быть в прошлом");
        }

        if (bookingShot.getEnd().isBefore(bookingShot.getStart())) {
            log.info("Дата окончания бронирования не может быть раньше даты начала бронирования");
            throw new InvalidParameterException("Дата окончания бронирования не может быть раньше даты начала бронирования");
        }
    }

    @SneakyThrows
    public static void isValidStart(BookingShot bookingShot) {
        if (bookingShot.getStart() == null) {
            log.info("Отсутствует дата начала бронирования");
            throw new InvalidParameterException("Отсутствует дата начала бронирования");
        }

        if (bookingShot.getStart().isBefore(now)) {
            log.info("Дата начала бронирования не может быть в прошлом");
            throw new InvalidParameterException("Дата начала бронирования не может быть в прошлом");
        }
    }

    @SneakyThrows
    public static Booking isValidBooking(Optional<Booking> booking) {
        if (!booking.isPresent()) {
            log.info("Бронирование не найдено");
            throw new NotFoundException("Бронирование не найдено");
        }
        return booking.get();
    }

}
