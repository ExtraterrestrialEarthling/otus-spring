package ru.otus.hw.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    @Transactional
    public Optional<Comment> findById(long id) {
        return commentRepository.findById(id);
    }

    @Override
    @Transactional
    public List<Comment> findByBookId(long bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    @Transactional
    public Comment insert(String text, long bookId) {
        return save(0, text, bookId);
    }

    @Override
    @Transactional
    public Comment update(long id, String text) {
        Optional<Comment> commentForUpdate = commentRepository.findById(id);
        if (commentForUpdate.isEmpty()) {
            throw new EntityNotFoundException("Комментарий для обновления с id %d не найден".formatted(id));
        }
        long bookId = commentForUpdate.get().getBook().getId();
        return save(id, text, bookId);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(long id, String text, long bookId) {
        if (text.isEmpty()){
            throw new IllegalStateException("Comment text cannot be empty");
        }
        Book book = bookRepository.findById(bookId).orElseThrow(()->
                new EntityNotFoundException("book with id %d not found".formatted(bookId)));
        return commentRepository.save(new Comment(id, text, book));
    }
}
