package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentJsonTest {
    @Autowired
    JacksonTester<Comment> json;

    List<Item> items = new ArrayList<>();
    LocalDateTime created = LocalDateTime.of(2021, 01, 15, 21, 07, 57);
    User author = new User(1L, "User 1", "1@ya.ru");
    Request request = new Request(1L, "Описание", items, author, created);
    Item item = new Item(1L, "Вещь", "Описание", true, author, request);

    @Test
    void testComment() throws Exception {
        Comment comment = new Comment(1L, "Комментарий", item, author, created);
        JsonContent<Comment> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment.getCreated().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.author").isNotNull();
    }

    @Test
    void testCommentNoArgs() throws Exception {
        Comment comment = new Comment();
        JsonContent<Comment> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isNull();
        assertThat(result).extractingJsonPathStringValue("$.text").isNull();
        assertThat(result).extractingJsonPathStringValue("$.created").isNull();
        assertThat(result).extractingJsonPathValue("$.item").isNull();
        assertThat(result).extractingJsonPathValue("$.author").isNull();
    }

    @Test
    void testCommentSetArgs() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("Комментарий");
        comment.setCreated(created);
        comment.setItem(item);
        comment.setAuthor(author);

        JsonContent<Comment> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment.getCreated().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.author").isNotNull();
    }

    @Test
    void testCommentBilder() throws Exception {
        Comment comment = Comment.builder()
                .id(1L)
                .text("Комментарий")
                .created(created)
                .item(item)
                .author(author)
                .build();

        JsonContent<Comment> result = json.write(comment);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(comment.getCreated().toString());
        assertThat(result).extractingJsonPathValue("$.item").isNotNull();
        assertThat(result).extractingJsonPathValue("$.author").isNotNull();
    }

}
