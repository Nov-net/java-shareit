package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Validated
public class ItemController {
    private final ItemClient itemClient;

    /*GET /items - получение списка всех вещей*/
    @GetMapping
    public ResponseEntity<Object> getItemsDtoByUserId(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                    @Positive @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        log.info("Получен запрос get item userId={}, from={}, size={}", userId, from, size);
        return itemClient.getItemsDtoByUserId(userId, from, size);
    }

    /*POST /items - создание новой вещи*/
    @PostMapping
    public ResponseEntity<Object> saveNewItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody @Valid ItemDto itemDto) {
        log.info("Получен запрос post item {}, userId={}", itemDto, userId);
        return itemClient.saveNewItem(userId, itemDto);
    }

    /*PATCH /items/{itemId} - обновление вещи*/
    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                                    @RequestBody @Valid ItemDto itemDto) {
        log.info("Получен запрос patch itemId={}, userId={}, item {}", itemId, userId, itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    /*GET /items/{itemId} - получение вещи по id*/
    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemDtoByItemId(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable  long itemId) {
        log.info("Получен запрос get item userId={}, itemId={}", userId, itemId);
        return itemClient.getItemDtoByItemId(userId, itemId);
    }

    /*GET /items/search?text={text} - поиск вещей*/
    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam String text,
                                    @PositiveOrZero @RequestParam(value = "from", defaultValue = "0", required = false) Integer from,
                                    @Positive @RequestParam(value = "size", defaultValue = "10", required = false) Integer size) {
        log.info("Получен запрос get search item text={}, from={}, size={}", text, from, size);
        return itemClient.searchItems(text, from, size);
    }

    /*POST /items/{itemId}/comment - добавление комментария*/
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @PathVariable(required = false)  long itemId,
                                    @RequestBody CommentDto text) {
        log.info("Получен запрос post comment {}, itemId={}, userId={}", text, itemId, userId);
        return itemClient.addComment(userId, itemId, text);
    }

}
