package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	/*POST /bookings - создание нового бронирования*/
	@PostMapping
	public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
							@RequestBody @Valid BookItemRequestDto requestDto) {
		log.info("Creating booking {}, userId={}", requestDto, userId);
		return bookingClient.bookItem(userId, requestDto);
	}

	/*GET /bookings/{bookingId} - получение бронирования по id*/
	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
							@PathVariable Long bookingId) {
		log.info("Get booking {}, userId={}", bookingId, userId);
		return bookingClient.getBooking(userId, bookingId);
	}

	/*GET /bookings?state={state} - получение списка всех бронирований по bookerId*/
	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
							@RequestParam(name = "state", defaultValue = "ALL", required = false) String stateParam,
							@PositiveOrZero @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
							@Positive @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);

		return bookingClient.getBookings(userId, stateParam, from, size);
	}

	/*GET /bookings/owner?state={state} - получение списка всех бронирований по ownerId*/
	@GetMapping("/owner")
	public ResponseEntity<Object> getBookingDtoByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
							@RequestParam(name = "state", defaultValue = "ALL", required = false) String stateParam,
							@PositiveOrZero @RequestParam(name = "from", defaultValue = "0", required = false) Integer from,
							@Positive @RequestParam(name = "size", defaultValue = "10", required = false) Integer size) {

		return bookingClient.getBookingDtoByOwnerId(userId, stateParam, from, size);
	}

	/*PATCH /bookings/:bookingId?approved=true - подтверждение бронирования*/
	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
							@PathVariable long bookingId,
							@RequestParam Boolean approved) {
		return bookingClient.approveBooking(userId, bookingId, approved);
	}

}
