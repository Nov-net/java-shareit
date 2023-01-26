package ru.practicum.shareit.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDtoForItemDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Long bookerId;
    Status status;

}
