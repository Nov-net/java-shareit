package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestDtoShot;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = RequestController.class)
public class RequestControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    RequestService service;

    @Autowired
    MockMvc mvc;

    LocalDateTime now = LocalDateTime.of(2023, 01, 15, 21, 07, 57);
    List<ItemDtoForRequest> items = new ArrayList<>();
    RequestDto requestDto = new RequestDto(1L, "Описание", now, items);
    RequestDtoShot requestDtoShot = new RequestDtoShot(1L, "Описание", now);

    @Test
    void saveNewRequestTest() throws Exception {
        when(service.saveNewRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(requestDtoShot))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated().toString())))
                .andExpect(jsonPath("$.items", is(requestDto.getItems())));
    }

    @Test
    void saveNewNotValidRequestTest() throws Exception {
        when(service.saveNewRequest(anyLong(), any()))
                .thenThrow(InvalidParameterException.class);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getRequestDtoByRequestIdTest() throws Exception {
        when(service.getRequestDtoByRequestId(anyLong(), anyLong()))
                .thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated().toString())))
                .andExpect(jsonPath("$.items", is(requestDto.getItems())));

    }

    @Test
    void getNotValidRequestByIdTest() throws Exception {
        when(service.getRequestDtoByRequestId(anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/requests/1"))
                .andExpect(status().isBadRequest());
    }

    @Test void getAllRequestTest() throws Exception {
        when(service.getAllRequest(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(requestDto.getCreated().toString())))
                .andExpect(jsonPath("$[0].items", is(requestDto.getItems())));
    }

    @Test void getRequestDtoByUserIdTest() throws Exception {
        when(service.getRequestDtoByUserId(anyLong()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(requestDto.getCreated().toString())))
                .andExpect(jsonPath("$[0].items", is(requestDto.getItems())));
    }

}
