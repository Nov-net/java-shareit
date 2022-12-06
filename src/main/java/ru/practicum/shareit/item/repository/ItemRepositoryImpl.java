package ru.practicum.shareit.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Primary
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final UserRepository userRepository;
    private static final List<Item> items = new ArrayList<>();
    private long lastId = 0;

    /*GET /items - получение списка вещей по userId*/
    @Override
    public List<ItemDto> getItemsDtoByUserId(long userId) {
        return items.stream()
                .filter(u -> u.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    /*POST /items - создание новой вещи*/
    @Override
    public ItemDto saveNewItem(long userId, ItemDto itemDto) {
        log.info("Запрос на добавление новой itemDto {}", itemDto);
        User user = userRepository.findUserById(userId);
        ItemValidator.isValidItem(itemDto);
        itemDto.setId(getId());
        log.info("Установлен id itemDto {}", itemDto.getId());
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        items.add(item);

        return itemDto;
    }

    /*PATCH /items/{itemId} - обновление вещи*/
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        log.info("Обновление itemDto {}", itemDto);
        User user = userRepository.findUserById(userId);
        Item item = findItemByItemId(itemId);
        if (user.getId().equals(item.getOwner().getId())) {
            items.remove(item);
        } else {
            throw new NotFoundException("Вещь другого пользователя");
        }

        if(itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if(itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if(itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        items.add(item);
        return ItemMapper.toItemDto(item);
    }

    /*GET /items/{itemId} - получение вещи по itemId*/
    @Override
    public ItemDto getItemDtoByItemId(long userId, long itemId) {
        return ItemMapper.toItemDto(findItemByItemId(itemId));
    }

    /*GET /items/search?text={text} - поиск вещей*/
    @Override
    public List<ItemDto> searchItems(String text) {
        List<ItemDto> list = new ArrayList<>();
        if (text.isEmpty()) {
            return list;

        }
        return items.stream()
                .filter(u -> u.getName().toLowerCase().contains(text.toLowerCase())
                        || u.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(i -> i.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    /*приват - поиск вещи по itemId*/
    private Item findItemByItemId(long itemId) {
        Optional<Item> item = items.stream()
                .filter(u -> u.getId() == itemId)
                .findFirst();

        if (item.isPresent()) {
            return item.get();
        } else {
            throw new NotFoundException("Вещь не найдена");
        }
    }

    /*приват - получение нового id*/
    private long getId() {
        lastId++;
        return lastId;
    }

}
