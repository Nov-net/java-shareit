package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShot;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    /*POST /bookings - создание нового бронирования*/
    @PostMapping
    public BookingDto saveNewBooking(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody BookingShot bookingShot) {
        return bookingService.saveNewBooking(userId, bookingShot);
    }

    /*GET /bookings/{bookingId} - получение бронирования по id*/
    @GetMapping("/{bookingId}")
    public BookingDto getBookingDtoByBookingId(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @PathVariable long bookingId) {
        return bookingService.getBookingDtoByBookingId(userId, bookingId);
    }

    /*GET /bookings?state={state} - получение списка всех бронирований по bookerId*/
    @GetMapping
    public List<BookingDto> getAllBookingDtoByBookerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
                                @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return bookingService.getAllBookingDtoByBookerId(userId, state, from, size);
    }

    /*GET /bookings/owner?state={state} - получение списка всех бронирований по ownerId*/
    @GetMapping("/owner")
    public List<BookingDto> getBookingDtoByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                @RequestParam(value = "state", defaultValue = "ALL", required = false) String state,
                                @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return bookingService.getBookingDtoByOwnerId(userId, state, from, size);
    }

    /*PATCH /bookings/:bookingId?approved=true - подтверждение бронирования*/
    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PathVariable long bookingId,
                                                   @RequestParam Boolean approved) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

}
