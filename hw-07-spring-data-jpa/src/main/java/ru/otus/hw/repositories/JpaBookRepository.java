//package ru.otus.hw.repositories;
//
//import jakarta.persistence.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//import ru.otus.hw.models.Book;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;
//
//@Repository
//@RequiredArgsConstructor
//public class JpaBookRepository implements BookRepository {
//
//    @PersistenceContext
//    private final EntityManager em;
//
//
//    @Override
//    public Optional<Book> findById(long id) {
//        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genres-entity-graph");
//        TypedQuery<Book> query = em.createQuery("select distinct b from Book b " +
//                "where b.id = :id", Book.class);
//        query.setParameter("id", id);
//        query.setHint(FETCH.getKey(), entityGraph);
//        return query.getResultStream().findFirst();
//    }
//
//    @Override
//    public List<Book> findAll() {
//        EntityGraph<?> entityGraph = em.getEntityGraph("book-author-genres-entity-graph");
//        TypedQuery<Book> query = em.createQuery("select distinct b from Book b ", Book.class);
//        query.setHint(FETCH.getKey(), entityGraph);
//        return query.getResultList();
//    }
//
//    @Override
//    public Book save(Book book) {
//        if(book.getId() != 0){
//            return em.merge(book);
//        } else {
//            em.persist(book);
//            return book;
//        }
//    }
//
//    @Override
//    public void deleteById(long id) {
//        Query query = em.createQuery("delete from Book b where b.id = :id");
//        query.setParameter("id", id);
//        query.executeUpdate();
//        em.clear();
//    }
//}
