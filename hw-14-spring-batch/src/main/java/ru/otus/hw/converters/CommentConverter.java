package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.nosql.Comment;

@Component
@RequiredArgsConstructor
public class CommentConverter {

    public String commentToString(Comment comment) {
        return "Id: %s, text: \"%s\", book: \"%s\"".formatted(
                comment.getId(), comment.getText(),
                comment.getBook().getTitle()
        );
    }
}
