package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.dto.BookingShot;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.itemBilder(booking.getItem()))
                .booker(UserMapper.userBilder(booking.getBooker()))
                .status(booking.getStatus())
                .build();
    }

    public static BookingDtoForItemDto toBookingDtoForItemDto(Booking booking) {
        return BookingDtoForItemDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapper.itemBilder(booking.getItem()))
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingShot bookingShot) {
        return Booking.builder()
                .start(bookingShot.getStart())
                .end(bookingShot.getEnd())
                .build();
    }

}
