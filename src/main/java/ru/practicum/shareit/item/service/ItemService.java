package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsDtoByUserId(long userId);
    ItemDto saveNewItem(long userId, ItemDto itemDto);
    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);
    ItemDto getItemDtoByItemId(long userId, long itemId);
    List<ItemDto> searchItems(String text);

}
