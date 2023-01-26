package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentDtoShotJsonTest {
    @Autowired
    JacksonTester<CommentDtoShot> json;

    @Test
    void testComment() throws Exception {
        CommentDtoShot comment = new CommentDtoShot("Комментарий");
        JsonContent<CommentDtoShot> result = json.write(comment);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
    }

    @Test
    void testCommentNoArgs() throws Exception {
        CommentDtoShot comment = new CommentDtoShot();
        JsonContent<CommentDtoShot> result = json.write(comment);

        assertThat(result).extractingJsonPathStringValue("$.text").isNull();
    }

    @Test
    void testCommentSetArgs() throws Exception {
        CommentDtoShot comment = new CommentDtoShot();
        comment.setText("Комментарий");

        JsonContent<CommentDtoShot> result = json.write(comment);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
    }

    @Test
    void testCommentBilder() throws Exception {
        CommentDtoShot comment = CommentDtoShot.builder()
                .text("Комментарий")
                .build();

        JsonContent<CommentDtoShot> result = json.write(comment);

        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(comment.getText());
    }

}
