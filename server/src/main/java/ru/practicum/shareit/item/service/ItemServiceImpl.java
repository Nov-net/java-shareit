package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.validator.ItemValidator;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.validator.RequestValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;

    /*GET /items - получение списка всех вещей*/
    @Override
    public List<ItemDto> getItemsDtoByUserId(long userId, Integer from, Integer size) {
        log.info("Запрос на получение всех вещей пользователя с id {}", userId);

        RequestValidator.isValidParameters(from, size);

        Pageable pageable = PageRequest.of(from / size, size);
        List<Item> listItem = repository.findAllByOwnerIdIsOrderById(userId, pageable).toList();
        log.info("Получили все вещи пользователя с id {}: {}", userId, listItem);

        List<ItemDto> listItemDto = ItemMapper.mapToItemDto(listItem);
        log.info("Конвертировали список в ItemDto: {}", listItem);

        LocalDateTime dateTime = LocalDateTime.now();

        return listItemDto.stream()
                .map(itemDto -> setCommentsAndLastAndNextBookingForItemDto(itemDto, dateTime))
                .collect(Collectors.toList());
    }


    /*POST /items - создание новой вещи*/
    @Override
    public ItemDto saveNewItem(long userId, ItemDto itemDto) {
        log.info("Запрос на добавление новой itemDto {} от пользователя с id {}", itemDto, userId);

        Optional<User> userOptional = userRepository.findById(userId);
        User user = UserValidator.isValidUser(userOptional);

        ItemValidator.isValidItem(itemDto);

        Item item = ItemMapper.toItem(itemDto);

        if (itemDto.getRequestId() != null) {
            Optional<Request> requestOptional = requestRepository.findById(itemDto.getRequestId());
            Request request = RequestValidator.isValidRequest(requestOptional);
            item.setRequest(request);
        }

        item.setOwner(user);
        Item newItem = repository.save(item);
        log.info("Добавлена новая item {}", newItem);
        return ItemMapper.toItemDto(newItem);
    }

    /*PATCH /items/{itemId} - обновление пользователя*/
    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        log.info("Запрос на обновление itemDto {} от пользователя с id {}", itemDto, userId);

        Optional<User> userOptional = userRepository.findById(userId);
        User user = UserValidator.isValidUser(userOptional);

        log.info("Получение вещи по id {}", itemId);
        Optional<Item> itemOptional = repository.findById(itemId);
        Item item = ItemValidator.isValidItem(itemOptional);
        log.info("Нашли и проверили вещь: {}", item);

        ItemValidator.isValidOwner(user, item);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
            log.info("Обновление имени на {}", item.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
            log.info("Обновление описания на {}", item.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
            log.info("Обновление доступности на {}", item.getAvailable());
        }

        if (itemDto.getRequestId() != null) {
            Optional<Request> requestOptional = requestRepository.findById(itemDto.getRequestId());
            Request request = RequestValidator.isValidRequest(requestOptional);
            item.setRequest(request);
            log.info("Обновление запроса на {}", itemDto.getRequestId());
        }

        log.info("Обновляем вещь с id {}, параметры item {}", item.getId(), item);
        Item updateItem = repository.save(item);
        log.info("item обновлена {}", updateItem);
        return ItemMapper.toItemDto(updateItem);
    }

    /*GET /items/{itemId} - получение вещи по id*/
    @Override
    public ItemDto getItemDtoByItemId(long userId, long itemId) {
        log.info("Запрошена item id {} по user id {}", itemId, userId);
        Item item = repository.getById(itemId);

        log.info("Получена item id {} c user id {}", item.getId(), item.getOwner().getId());

        ItemDto itemDto = ItemMapper.toItemDto(item);

        if (item.getOwner().getId().equals(userId)) {
            LocalDateTime dateTime = LocalDateTime.now();
            setCommentsAndLastAndNextBookingForItemDto(itemDto, dateTime);
        } else {
            itemDto.setComments(CommentMapper.mapToCommentDto(commentRepository.findAllByItemIdIsOrderById(itemDto.getId())));
        }

        return itemDto;
    }

    /*GET /items/search?text={text} - поиск вещей*/
    @Override
    public List<ItemDto> searchItems(String text, Integer from, Integer size) {
        log.info("Запрос на поиск вещи: {}", text);
        List<ItemDto> list = new ArrayList<>();
        if (text.isEmpty()) {
            log.info("Запрос на поиск вещи пустой");
            return list;
        }

        RequestValidator.isValidParameters(from, size);
        Pageable pageable = PageRequest.of(from / size, size);

        List<Item> items = repository.search(text, pageable).toList();
        return items.stream()
                .filter(i -> i.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    /*POST /items/{itemId}/comment - добавление комментария*/
    public CommentDto addComment(long userId, long itemId, CommentDto text) {
        log.info("Запрос на добавление комментария от пользователя id={} для вещи id={}, текст комментария: {}",
                userId, itemId, text.getText());
        ItemValidator.isValidText(text.getText());

        Optional<User> userOptional = userRepository.findById(userId);
        User user = UserValidator.isValidUser(userOptional);
        log.info("Нашли и проверили пользователя: {}", user);

        log.info("Получение вещи по id {}", itemId);
        Optional<Item> itemOptional = repository.findById(itemId);
        Item item = ItemValidator.isValidItem(itemOptional);
        log.info("Нашли и проверили вещь: {}", item);

        List<Booking> bookings = bookingRepository.findByBooker_IdAndOrderByStart_DateDesc(userId);
        log.info("Нашли лист бронирований для вещи: {}", bookings);

        Boolean bookingsIsPresent = bookings.stream()
                .filter(i -> i.getItem().getId().equals(itemId))
                .anyMatch(i -> i.getEnd().isBefore(LocalDateTime.now()));

        if (Boolean.FALSE.equals(bookingsIsPresent)) {
            log.info("Бронирования вещи с id {} пользователем с id {} не найдены", itemId, userId);
            throw new InvalidParameterException("Бронирования не найдены");
        } else {
            Comment comment = new Comment(null, text.getText(), item, user, LocalDateTime.now());
            commentRepository.save(comment);
            log.info("Создали и сохранили новый коммент: {}", comment);
            return CommentMapper.toCommentDto(comment);
        }

    }

    /*Добавление списка комментов, последнего и следующего бронирования в itemDto*/
    private ItemDto setCommentsAndLastAndNextBookingForItemDto(ItemDto itemDto, LocalDateTime dateTime) {
        itemDto.setComments(CommentMapper.mapToCommentDto(commentRepository.findAllByItemIdIsOrderById(itemDto.getId())));
        log.info("Установили ItemDto {} comments: {}", itemDto.getId(), itemDto.getComments());

        itemDto.setLastBooking(lastBookingForItemDto(itemDto, dateTime));
        log.info("Установили ItemDto {} last бронирование: {}", itemDto.getId(), itemDto.getLastBooking());

        itemDto.setNextBooking(nextBookingForItemDto(itemDto, dateTime));
        log.info("Установили ItemDto {} next бронирование: {}", itemDto.getId(), itemDto.getNextBooking());
        return itemDto;
    }

    /*Получение последнего бронирования для itemDto*/
    private BookingDtoForItemDto lastBookingForItemDto(ItemDto itemDto, LocalDateTime dateTime) {
        Optional<Booking> booking = bookingRepository.findLastBookingByItem_IdOrderByEnd_DateDescLimitOne(itemDto.getId(), dateTime);
        if (!booking.isPresent()) {
            log.info("Прошлого бронирования нет");
            return null;
        }

        log.info("Получили last бронирование для ItemDto {}: {}", itemDto.getId(), booking.get());
        return BookingMapper.toBookingDtoForItemDto(booking.get());
    }

    /*Получение следующего бронирования для itemDto*/
    private BookingDtoForItemDto nextBookingForItemDto(ItemDto itemDto, LocalDateTime dateTime) {
        Optional<Booking> booking = bookingRepository.findNextBookingByItem_IdOrderByStart_DateDescLimitOne(itemDto.getId(), dateTime);
        if (!booking.isPresent()) {
            log.info("Будущего бронирования нет");
            return null;
        }

        log.info("Получили next бронирование для ItemDto {}: {}", itemDto.getId(), booking.get());
        return BookingMapper.toBookingDtoForItemDto(booking.get());
    }

}
