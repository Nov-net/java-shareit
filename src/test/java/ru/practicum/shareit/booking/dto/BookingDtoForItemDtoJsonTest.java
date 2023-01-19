package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoForItemDtoJsonTest {
    @Autowired
    JacksonTester<BookingDtoForItemDto> json;

    List<Item> items = new ArrayList<>();
    LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
    User user = new User(1L, "User 1", "1@ya.ru");
    Request request = new Request(1L, "Описание", items, user, now);
    Item item = new Item(1L, "Вещь", "Описание", true, user, request);

    @Test
    void testBooking() throws Exception {
        BookingDtoForItemDto booking = new BookingDtoForItemDto(1L, now, now, item, 1L, Status.APPROVED);
        JsonContent<BookingDtoForItemDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.bookerId").isEqualTo(1);
    }

    @Test
    void testBookingDtoNoArgs() throws Exception {
        BookingDtoForItemDto booking = new BookingDtoForItemDto();
        JsonContent<BookingDtoForItemDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isNull();
        assertThat(result).extractingJsonPathStringValue("$.start").isNull();
        assertThat(result).extractingJsonPathStringValue("$.end").isNull();
        assertThat(result).extractingJsonPathValue("$.item").isNull();
        assertThat(result).extractingJsonPathValue("$.bookerId").isNull();
    }

    @Test
    void testBookingDtoSetArgs() throws Exception {
        BookingDtoForItemDto booking = new BookingDtoForItemDto();
        booking.setId(1L);
        booking.setStart(now);
        booking.setEnd(now);
        booking.setItem(item);
        booking.setBookerId(1L);
        booking.setStatus(Status.APPROVED);

        JsonContent<BookingDtoForItemDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(booking.getStatus().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.bookerId").isEqualTo(1);
    }

    @Test
    void testBookingDtoBilder() throws Exception {
        BookingDtoForItemDto booking = BookingDtoForItemDto.builder()
                .id(1L)
                .start(now)
                .end(now)
                .item(item)
                .bookerId(1L)
                .status(Status.APPROVED)
                .build();

        JsonContent<BookingDtoForItemDto> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(booking.getStatus().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.bookerId").isEqualTo(1);
    }

}
