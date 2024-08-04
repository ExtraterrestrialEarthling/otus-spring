package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dto.request.CommentDtoRq;
import ru.otus.hw.dto.response.CommentDtoRs;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.mapper.CommentMapper;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public CommentDtoRs findById(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Comment with id %d not found".formatted(id)));
        return commentMapper.entityToDtoRs(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDtoRs> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId)
                .stream()
                .map(commentMapper::entityToDtoRs)
                .toList();
    }

    @Override
    @Transactional
    public CommentDtoRs insert(CommentDtoRq commentDtoRq) {
        return commentMapper.entityToDtoRs(save(commentDtoRq));
    }

    @Override
    @Transactional
    public CommentDtoRs update(CommentDtoRq commentDtoRq) {
        Optional<Comment> commentForUpdate = commentRepository.findById(commentDtoRq.getId());
        if (commentForUpdate.isEmpty()) {
            throw new EntityNotFoundException("Комментарий для обновления с id %d не найден"
                    .formatted(commentDtoRq.getId()));
        }
        return commentMapper.entityToDtoRs(save(commentDtoRq));
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(CommentDtoRq commentDto) {
        Book book = bookRepository.findById(commentDto.getBookId()).orElseThrow(() ->
                new EntityNotFoundException("Book with id %d not found".formatted(commentDto.getBookId())));
        return commentRepository.save(new Comment(commentDto.getId(),
                commentDto.getText(),
                book));
    }
}
