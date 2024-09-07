package ru.otus.hw.mapper;

import org.mapstruct.Mapper;
import ru.otus.hw.dto.response.CommentDtoRs;
import ru.otus.hw.models.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    default CommentDtoRs entityToDtoRs(Comment comment) {
        return new CommentDtoRs(comment.getId(), comment.getText(), comment.getBook().getId());
    }
}
