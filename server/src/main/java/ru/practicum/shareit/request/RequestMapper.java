package ru.practicum.shareit.request;

import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoShot;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        List<ItemDtoForRequest> items = ItemMapper.mapToItemDtoForRequest(request.getItems());
        return RequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(items != null ? items : null)
                .build();
    }

    public static List<RequestDto> mapToRequestDto(List<Request> requests) {
        if (requests == null) {
            return null;
        }

        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    public static Request toRequest(RequestDtoShot requestShot, User user) {
        return Request.builder()
                .description(requestShot.getDescription())
                .requestor(UserMapper.userBilder(user))
                .created(LocalDateTime.now())
                .build();
    }

    public static Request requestBilder (Request request) {
        return Request.builder()
                .id(request.getId())
                .description(request.getDescription())
                .items(request.getItems() != null ? request.getItems() : null)
                .requestor(request.getRequestor())
                .created(request.getCreated())
                .build();
    }
}
