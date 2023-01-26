package ru.practicum.shareit.request.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestDtoShot {
    private Long requestorId;
    private String description;
    private LocalDateTime created;

}
