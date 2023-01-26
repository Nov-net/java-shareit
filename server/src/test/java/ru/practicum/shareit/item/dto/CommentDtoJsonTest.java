package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoJsonTest {
    @Autowired
    JacksonTester<CommentDto> json;

   LocalDateTime created = LocalDateTime.of(2021, 01, 15, 21, 07, 57);

    @Test
    void testComment() throws Exception {
        CommentDto comment = new CommentDto(1L, "Комментарий", "Имя автора", created);
        JsonContent<CommentDto> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(comment.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment.getCreated().toString());
    }

    @Test
    void testCommentNoArgs() throws Exception {
        CommentDto comment = new CommentDto();
        JsonContent<CommentDto> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isNull();
        assertThat(result).extractingJsonPathStringValue("$.text").isNull();
        assertThat(result).extractingJsonPathStringValue("$.created").isNull();
        assertThat(result).extractingJsonPathStringValue("$.authorName").isNull();
    }

    @Test
    void testCommentSetArgs() throws Exception {
        CommentDto comment = new CommentDto();
        comment.setId(1L);
        comment.setText("Комментарий");
        comment.setCreated(created);
        comment.setAuthorName("Имя автора");

        JsonContent<CommentDto> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(comment.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment.getCreated().toString());
    }

    @Test
    void testCommentBilder() throws Exception {
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("Комментарий")
                .created(created)
                .authorName("Имя автора")
                .build();

        JsonContent<CommentDto> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(comment.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment.getCreated().toString());
    }

}
