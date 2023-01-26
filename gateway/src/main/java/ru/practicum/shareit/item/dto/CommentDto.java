package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private String authorName;
}
