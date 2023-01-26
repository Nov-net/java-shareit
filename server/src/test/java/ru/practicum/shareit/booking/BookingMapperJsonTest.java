package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.dto.BookingShot;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingMapperJsonTest {
    @Autowired
    JacksonTester<BookingDto> json;
    @Autowired
    JacksonTester<BookingDtoForItemDto> json2;
    @Autowired
    JacksonTester<Booking> json3;

    List<Item> items = new ArrayList<>();
    LocalDateTime start = LocalDateTime.of(2023, 02, 15, 21, 07, 57);
    LocalDateTime end = LocalDateTime.of(2023, 03, 15, 21, 07, 57);
    User user = new User(1L, "User 1", "1@ya.ru");
    Request request = new Request(1L, "Описание", items, user, start);
    Item item = new Item(1L, "Вещь", "Описание", true, user, request);
    BookingShot bookingShot = new BookingShot(1L, start, end);
    Booking booking = new Booking(1L, start, end, item, user, Status.WAITING);

    @Test
    void toBookingDtoTest() throws Exception {
        BookingDto bookingDto = BookingMapper.toBookingDto(booking);
        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.booker").isNotNull();
    }

    @Test
    void toBookingDtoForItemDtoTest() throws Exception {
        BookingDtoForItemDto bookingDto = BookingMapper.toBookingDtoForItemDto(booking);
        JsonContent<BookingDtoForItemDto> result = json2.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.bookerId").isEqualTo(1);
    }

    @Test
    void toBookingTest() throws Exception {
        Booking booking = BookingMapper.toBooking(bookingShot);
        JsonContent<Booking> result = json3.write(booking);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
    }

}
