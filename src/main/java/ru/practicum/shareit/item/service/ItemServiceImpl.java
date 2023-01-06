package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    /*GET /items - получение списка всех вещей*/
    @Override
    public List<ItemDto> getItemsDtoByUserId(long userId) {
        return repository.getItemsDtoByUserId(userId);
    }

    /*POST /items - создание новой вещи*/
    @Override
    public ItemDto saveNewItem(long userId, ItemDto itemDto) {
        return repository.saveNewItem(userId, itemDto);
    }

    /*PATCH /items/{itemId} - обновление пользователя*/
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        return repository.updateItem(userId, itemId, itemDto);
    }

    /*GET /items/{itemId} - получение вещи по id*/
    @Override
    public ItemDto getItemDtoByItemId(long userId, long itemId) {
        return repository.getItemDtoByItemId(userId, itemId);
    }

    /*GET /items/search?text={text} - поиск вещей*/
    @Override
    public List<ItemDto> searchItems(String text) {
        return repository.searchItems(text);
    }

}
