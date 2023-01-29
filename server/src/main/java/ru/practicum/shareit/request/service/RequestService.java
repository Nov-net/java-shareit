package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoShot;

import java.util.List;

public interface RequestService {
    RequestDto saveNewRequest(long userId, RequestDtoShot request);

    List<RequestDto> getRequestDtoByUserId(long userId);

    RequestDto getRequestDtoByRequestId(long userId, long requestId);

    List<RequestDto> getAllRequest(Long userId, Integer from, Integer size);
}
