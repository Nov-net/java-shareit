package ru.practicum.shareit.booking.validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShot;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JsonTest
public class BookingValidatorJsonTest {
    @Autowired
    private JacksonTester<Booking> json;
    BookingValidator validator = new BookingValidator();
    LocalDateTime now = LocalDateTime.of(2022, 01, 14, 21, 07, 57);
    LocalDateTime start = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
    BookingShot bookingShot1 = new BookingShot(null, null, null);
    BookingShot bookingShot2 = new BookingShot(1L, start, now);

    @Test
    void isValidItemIdTest() throws Exception {
        assertThrows(InvalidParameterException.class, () -> validator.isValidItemId(bookingShot1));
    }

    @Test
    void isValidEndTest() throws Exception {
        assertThrows(InvalidParameterException.class, () -> validator.isValidEnd(bookingShot1));
        assertThrows(InvalidParameterException.class, () -> validator.isValidEnd(bookingShot2));

        LocalDateTime start = LocalDateTime.of(2023, 11, 15, 21, 07, 57);
        LocalDateTime end = LocalDateTime.of(2023, 12, 15, 21, 07, 57);
        BookingShot bookingShot3 = new BookingShot(null, end, start);
        assertThrows(InvalidParameterException.class, () -> validator.isValidEnd(bookingShot3));
    }

    @Test
    void isValidStartTest() throws Exception {
        assertThrows(InvalidParameterException.class, () -> validator.isValidStart(bookingShot1));
        assertThrows(InvalidParameterException.class, () -> validator.isValidStart(bookingShot2));
    }

    @Test
    void isValidBookinTest() throws Exception {
        assertThrows(InvalidParameterException.class, () -> validator.isValidBooking(bookingShot1));

        assertThrows(InvalidParameterException.class, () -> validator.isValidBooking(bookingShot2));

        LocalDateTime start = LocalDateTime.of(2023, 11, 15, 21, 07, 57);
        LocalDateTime end = LocalDateTime.of(2023, 12, 15, 21, 07, 57);
        BookingShot bookingShot3 = new BookingShot(null, end, start);
        assertThrows(InvalidParameterException.class, () -> validator.isValidBooking(bookingShot3));
    }

    @Test
    void isValidBookingTest() throws Exception { List<Item> items = new ArrayList<>();
        LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
        User user = new User(1L, "User 1", "1@ya.ru");
        Request request = new Request(1L, "Описание", items, user, now);
        Item item = new Item(1L, "Вещь", "Описание", true, user, request);
        Booking booking = new Booking(1L, now, now, item, user, Status.APPROVED);

        Optional<Booking> bookingOptional = Optional.ofNullable(booking);

        Booking booking2 = validator.isValidBooking(bookingOptional);

        JsonContent<Booking> result = json.write(booking2);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.booker").isNotNull();
    }

    @Test
    void UserValidatorNotFoundExceptionTest() {
        Optional<Booking> bookingOptional = Optional.ofNullable(null);

        assertThrows(NotFoundException.class, () -> validator.isValidBooking(bookingOptional));
    }

}
