package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    /*GET /items - получение списка всех вещей*/
    @GetMapping
    public List<ItemDto> getItemsDtoByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsDtoByUserId(userId);
    }

    /*POST /items - создание новой вещи*/
    @PostMapping
    public ItemDto saveNewItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
        return itemService.saveNewItem(userId, itemDto);
    }

    /*PATCH /items/{itemId} - обновление вещи*/
    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    /*GET /items/{itemId} - получение вещи по id*/
    @GetMapping("/{itemId}")
    public ItemDto getItemDtoByItemId(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable(required = false)  long itemId) {
        return itemService.getItemDtoByItemId(userId, itemId);
    }

    /*GET /items/search?text={text} - поиск вещей*/
    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    /*POST /items/{itemId}/comment - добавление комментария*/
    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @PathVariable(required = false)  long itemId,
                                 @RequestBody CommentDto text) {
        return itemService.addComment(userId, itemId, text);
    }

}
