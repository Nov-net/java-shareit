package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemsDtoByUserId(long userId, Integer from, Integer size);

    ItemDto saveNewItem(long userId, ItemDto itemDto);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    ItemDto getItemDtoByItemId(long userId, long itemId);

    List<ItemDto> searchItems(String text, Integer from, Integer size);

    CommentDto addComment(long userId, long itemId, CommentDto text);

}
