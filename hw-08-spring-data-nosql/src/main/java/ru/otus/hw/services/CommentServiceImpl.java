package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
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

    @Override
    @Transactional(readOnly = true)
    public Optional<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> findByBookId(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(String text, String bookId) {
        return save(null, text, bookId);
    }

    @Override
    @Transactional
    public Comment update(String id, String text) {
        Optional<Comment> commentForUpdate = commentRepository.findById(id);
        if (commentForUpdate.isEmpty()) {
            throw new EntityNotFoundException("Комментарий для обновления с id %s не найден".formatted(id));
        }
        String bookId = commentForUpdate.get().getBook().getId();
        return save(id, text, bookId);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private Comment save(String id, String text, String bookId) {
        if (text.isEmpty()) {
            throw new IllegalStateException("Comment text cannot be empty");
        }
        Book book = bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("book with id %s not found".formatted(bookId)));
        return commentRepository.save(new Comment(id, text, book));
    }
}
