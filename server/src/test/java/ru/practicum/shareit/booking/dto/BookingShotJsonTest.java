package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingShotJsonTest {
    @Autowired
    JacksonTester<BookingShot> json;

    LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);

    @Test
    void testBooking() throws Exception {
        BookingShot booking = new BookingShot(1L, now, now);
        JsonContent<BookingShot> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
    }

    @Test
    void testBookingDtoSetArgs() throws Exception {
        BookingShot booking = new BookingShot(1L, now, now);
        booking.setItemId(2L);
        booking.setStart(now);
        booking.setEnd(now);

        JsonContent<BookingShot> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
    }

    @Test
    void testBookingDtoBilder() throws Exception {
        BookingShot booking = BookingShot.builder()
                .itemId(1L)
                .start(now)
                .end(now)
                .build();

        JsonContent<BookingShot> result = json.write(booking);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(booking.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(booking.getEnd().toString());
    }

}
