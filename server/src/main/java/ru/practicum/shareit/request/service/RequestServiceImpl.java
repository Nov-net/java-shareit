package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.RequestMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoShot;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.validator.RequestValidator;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.validator.UserValidator;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Primary
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;

    /*POST /requests - добавление нового запроса*/
    @Override
    public RequestDto saveNewRequest(long userId, RequestDtoShot requestDto) {
        log.info("Добавление нового запроса {} от пользователя с id {}", requestDto, userId);

        log.info("Получение пользователя по id {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        User user = UserValidator.isValidUser(userOptional);

        RequestValidator.isValidDescription(requestDto);

        Request request = RequestMapper.toRequest(requestDto, user);
        Request newRequest = repository.save(request);
        log.info("Добавлен новый запрос {}", newRequest);

        return RequestMapper.toRequestDto(newRequest);
    }

    /*GET /requests - получение списка всех запросов по userId*/
    @Override
    public List<RequestDto> getRequestDtoByUserId(long requestorId) {
        log.info("Запрос на получение всех запросов пользователя с id {}", requestorId);

        log.info("Получение пользователя по id {}", requestorId);
        Optional<User> userOptional = userRepository.findById(requestorId);
        UserValidator.isValidUser(userOptional);

        List<Request> listRequest = repository.findAllByRequestorIdIsOrderByCreatedDesc(requestorId);
        log.info("Получили все вещи пользователя с id {}: {}", requestorId, listRequest);

        return RequestMapper.mapToRequestDto(listRequest);
    }

    /*GET /requests/{requestId} - получение запроса по id*/
    @Override
    public RequestDto getRequestDtoByRequestId(long userId, long requestId) {
        log.info("Получение запроса с id {}", requestId);

        log.info("Получение пользователя по id {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        UserValidator.isValidUser(userOptional);

        Optional<Request> requestOptional = repository.findById(requestId);

        if (!requestOptional.isPresent()) {
            log.info("Запрос не найден");
            throw new NotFoundException("Запрос не найден");
        }

        return RequestMapper.toRequestDto(requestOptional.get());
    }

    /*GET /requests/all?from={from}&size={size} - получение списка запросов по id*/
    public List<RequestDto> getAllRequest(Long userId, Integer from, Integer size) {
        log.info("Получение всех запросов со страницы {} в количестве {} пользователем с id {}", from, size, userId);
        RequestValidator.isValidParameters(from, size);

        log.info("Получение пользователя по id {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        UserValidator.isValidUser(userOptional);

        Pageable pageable = PageRequest.of(from / size, size);
        List<Request> list = null;
        if (repository.findAllIsOrderByCreatedDesc(pageable) != null) {
            list = repository.findAllIsOrderByCreatedDesc(pageable).toList();
        }

        if (list == null) {
            return null;
        }
        return list.stream()
                .filter(r -> !r.getRequestor().getId().equals(userId))
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

}
