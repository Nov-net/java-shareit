package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDtoForItemDto lastBooking;
    private BookingDtoForItemDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
