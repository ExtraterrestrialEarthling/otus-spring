package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.converters.CommentConverter;
import ru.otus.hw.services.CommentService;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class CommentCommands {

    private final CommentService commentService;

    private final CommentConverter commentConverter;

    @ShellMethod(value = "Find comment by id", key = "cbid")
    public String findCommentById(long id){
        return commentService.findById(id)
                .map(commentConverter::commentToString)
                .orElse("Comment with id %d not found".formatted(id));
    }

    @ShellMethod(value = "Find all comments by book id", key = "acbbid")
    public String findCommentByBookId(long bookId){
        return commentService.findByBookId(bookId).stream()
                .map(commentConverter::commentToString)
                .collect(Collectors.joining(", "));
    }

    //cins 1 "great book"
    @ShellMethod(value = "Create comment for book", key = "cins")
    public String createCommentForBook(long bookId, String text){
        return commentConverter.commentToString(commentService.insert(text, bookId));
    }

    @ShellMethod(value = "Delete comment by id", key = "cdel")
    public void deleteCommentById(long id){
        commentService.deleteById(id);
    }
}
