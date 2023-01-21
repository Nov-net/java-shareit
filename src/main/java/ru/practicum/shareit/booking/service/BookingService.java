package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShot;

import java.util.List;

public interface BookingService {
    BookingDto saveNewBooking(long userId, BookingShot bookingShot);

    BookingDto getBookingDtoByBookingId(long userId, long bookingId);

    List<BookingDto> getAllBookingDtoByBookerId(long userId, String state, Integer from, Integer size);

    List<BookingDto> getBookingDtoByOwnerId(long userId, String state, Integer from, Integer size);

    BookingDto approveBooking(long userId, long bookingId, Boolean approved);

}
