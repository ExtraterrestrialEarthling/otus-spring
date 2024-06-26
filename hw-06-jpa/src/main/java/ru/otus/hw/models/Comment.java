package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"book"})
@ToString(exclude = {"book"})
@NamedEntityGraph(name = "comment-book-entity-graph",
        attributeNodes = @NamedAttributeNode("book"))
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
