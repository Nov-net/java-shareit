package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

@JsonTest
public class CommentMapperJsonTest {
    @Autowired
    JacksonTester<CommentDto> json;

    LocalDateTime created = LocalDateTime.of(2021, 01, 15, 21, 07, 57);
    User author = new User(1L, "User 1", "1@ya.ru");
    Item item = new Item(1L, "Вещь", "Описание", true, author, null);
    Comment comment = new Comment(1L, "Комментарий", item, author, created);

    @Test
    void toCommentDtoTest() throws Exception {
        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        JsonContent<CommentDto> result = json.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathValue("$.authorName").isEqualTo(comment.getAuthor().getName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment.getCreated().toString());
    }

    @Test
    void mapToCommentDtoTest() throws Exception {
        List<CommentDto> commentDto = CommentMapper.mapToCommentDto(List.of(comment));
        JsonContent<CommentDto> result = json.write(commentDto.get(0));

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathValue("$.authorName").isEqualTo(comment.getAuthor().getName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment.getCreated().toString());
    }

}
