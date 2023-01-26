package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDtoShot;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class RequestController {
    private final RequestClient requestClient;

    /*POST /requests - добавление нового запроса*/
    @PostMapping
    public ResponseEntity<Object> saveNewRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestBody @Valid RequestDtoShot requestDto) {
        return requestClient.saveNewRequest(userId, requestDto);
    }

    /*GET /requests - получение списка всех запросов по userId*/
    @GetMapping
    public ResponseEntity<Object> getRequestDtoByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return requestClient.getRequestDtoByUserId(userId);
    }

    /*GET /requests/{requestId} - получение запроса по id*/
    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestDtoByRequestId(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable(required = false)  long requestId) {
        return requestClient.getRequestDtoByRequestId(userId, requestId);
    }

    /*GET /requests/all?from={from}&size={size} - получение списка запросов по id*/
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                    @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                    @Positive @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        return requestClient.getAllRequest(userId, from, size);
    }

}
