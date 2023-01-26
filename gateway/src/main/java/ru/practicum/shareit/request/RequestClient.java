package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestDtoShot;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    /*POST /requests - добавление нового запроса*/
    public ResponseEntity<Object> saveNewRequest(long userId, RequestDtoShot requestDto) {
        return post("", userId, requestDto);
    }

    /*GET /requests - получение списка всех запросов по userId*/
    public ResponseEntity<Object> getRequestDtoByUserId(long userId) {
        return get("", userId);
    }

    /*GET /requests/{requestId} - получение запроса по id*/
    public ResponseEntity<Object> getRequestDtoByRequestId(long userId, long requestId) {
        return get("/" + requestId, userId);
    }

    /*GET /requests/all?from={from}&size={size} - получение списка запросов по id*/
    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequest(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", userId, parameters);
    }

}
