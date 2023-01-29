package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;

}