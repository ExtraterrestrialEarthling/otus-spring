package ru.otus.hw.repositories;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Comment> findById(Long id) {
        EntityGraph<?> entityGraph = em.getEntityGraph("comment-book-entity-graph");
        TypedQuery<Comment> query = em.createQuery("select c from Comment c " +
                "where c.id = :id", Comment.class);
        query.setParameter("id", id);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultStream().findFirst();
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        EntityGraph<?> entityGraph = em.getEntityGraph("comment-book-entity-graph");
        TypedQuery<Comment> query = em.createQuery("select c from Comment c where c.book.id = :bookId", Comment.class);
        query.setParameter("bookId", bookId);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId()!=0){
            return em.merge(comment);
        } else {
            em.persist(comment);
            return comment;
        }
    }

    public void deleteById(long id) {
        Query query = em.createQuery("delete from Comment c where c.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
        em.clear();
    }
}
