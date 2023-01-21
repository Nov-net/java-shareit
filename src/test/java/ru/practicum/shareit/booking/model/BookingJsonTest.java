package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingJsonTest {
    @Autowired
    JacksonTester<Booking> json;

    List<Item> items = new ArrayList<>();
    LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
    User user = new User(1L, "User 1", "1@ya.ru");
    Request request = new Request(1L, "Описание", items, user, now);
    Item item = new Item(1L, "Вещь", "Описание", true, user, request);

    @Test
    void testBooking() throws Exception {
        Booking booking = new Booking(1L, now, now, item, user, Status.APPROVED);
        JsonContent<Booking> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.booker").isNotNull();
    }

    @Test
    void testBookingNoArgs() throws Exception {
        Booking booking = new Booking();
        JsonContent<Booking> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isNull();
        assertThat(result).extractingJsonPathStringValue("$.start").isNull();
        assertThat(result).extractingJsonPathStringValue("$.end").isNull();
        assertThat(result).extractingJsonPathValue("$.item").isNull();
        assertThat(result).extractingJsonPathValue("$.booker").isNull();
    }

    @Test
    void testBookingSetArgs() throws Exception {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStart(now);
        booking.setEnd(now);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(Status.APPROVED);

        JsonContent<Booking> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(booking.getStatus().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.booker").isNotNull();
    }

    @Test
    void testBookingBilder() throws Exception {
        Booking booking = Booking.builder()
                .id(1L)
                .start(now)
                .end(now)
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();

        JsonContent<Booking> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(booking.getStatus().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.booker").isNotNull();
    }

}
