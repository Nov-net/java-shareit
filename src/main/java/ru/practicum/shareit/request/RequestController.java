package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoShot;
import ru.practicum.shareit.request.service.RequestService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestService requestService;

    /*POST /requests - добавление нового запроса*/
    @PostMapping
    public RequestDto saveNewRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody RequestDtoShot requestDto) {
        return requestService.saveNewRequest(userId, requestDto);
    }

    /*GET /requests - получение списка всех запросов по userId*/
    @GetMapping
    public List<RequestDto> getRequestDtoByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestService.getRequestDtoByUserId(userId);
    }

    /*GET /requests/{requestId} - получение запроса по id*/
    @GetMapping("/{requestId}")
    public RequestDto getRequestDtoByRequestId(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable(required = false)  long requestId) {
        return requestService.getRequestDtoByRequestId(userId, requestId);
    }

    /*GET /requests/all?from={from}&size={size} - получение списка запросов по id*/
    @GetMapping("/all")
    public List<RequestDto> getAllRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                       @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return requestService.getAllRequest(userId, from, size);
    }

}
