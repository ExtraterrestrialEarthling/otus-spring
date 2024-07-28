package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @Test
    public void testListBooks() throws Exception {
        Book book = getTestBook();
        when(bookService.findAll()).thenReturn(List.of(book));
        this.mvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("books", List.of(book)));
    }

    @Test
    public void testGetBookDetails() throws Exception {
        long bookId = 1L;
        Book book = getTestBook();
        List<Comment> comments = getTestCommentsFor(book);
        when(bookService.findById(bookId)).thenReturn(book);
        when(commentService.findByBookId(bookId)).thenReturn(comments);
        this.mvc.perform(get("/books/details/" + bookId))
                .andExpect(status().isOk())
                .andExpect(view().name("details"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("comments", comments));
    }

    @Test
    public void testEditPage() throws Exception {
        Book book = getTestBook();
        List<Genre> genres = getTestGenres();
        when(bookService.findById(book.getId())).thenReturn(book);
        when(genreService.findAll()).thenReturn(genres);
        this.mvc.perform(get("/books/edit/" + book.getId()))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("genres", genres))
                .andExpect(view().name("edit"));
    }


    @Test
    public void testApplyChanges() throws Exception {
        Book book = getTestBook();
        String updatedTitle = "new title";
        String updatedAuthorName = "new author";
        Set<Long> genreIds = book.getGenres()
                .stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        when(bookService.update(book.getId(),
                updatedTitle,
                book.getAuthor().getId(),
                genreIds))
                .thenReturn(new Book(book.getId(), updatedTitle, book.getAuthor(), book.getGenres()));
        when(authorService.update(book.getAuthor().getId(), updatedAuthorName))
                .thenReturn(new Author(book.getAuthor().getId(), updatedAuthorName));
        this.mvc.perform(post("/books/edit")
                        .param("bookId", String.valueOf(book.getId()))
                        .param("title", updatedTitle)
                        .param("authorId", String.valueOf(book.getAuthor().getId()))
                        .param("authorFullName", updatedAuthorName)
                        .param("genreIds", genreIds.stream().map(String::valueOf).toArray(String[]::new)))
                .andExpect(redirectedUrl("/books/details/" + book.getId()));
    }

    @Test
    public void applyChangesShouldThrowErrorIfValidationFails() throws Exception {
        Book book = getTestBook();
        String updatedTitle = ""; //empty
        String updatedAuthorName = ""; //empty
        String genreIds = ""; //empty - type string because will be sent to params
        when(bookService.findById(book.getId())).thenReturn(book);
        when(genreService.findAll()).thenReturn(getTestGenres());
        this.mvc.perform(post("/books/edit")
                        .param("bookId", String.valueOf(book.getId()))
                        .param("title", updatedTitle)
                        .param("authorId", String.valueOf(book.getAuthor().getId()))
                        .param("authorFullName", updatedAuthorName)
                        .param("genreIds", genreIds))
                .andExpect(status().isOk())
                .andExpect(view().name("edit"))
                .andExpect(model().attributeHasFieldErrors("editBookDto",
                        "title", "authorFullName"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("genres", getTestGenres()))
                .andExpect(model().attributeExists("errors"));
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        Book book = getTestBook();
        this.mvc.perform(post("/books/delete/" + book.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).deleteById(book.getId());
    }

    @Test
    public void testAddComment() throws Exception {
        Book book = getTestBook();
        this.mvc.perform(post("/books/" + book.getId() + "/comments")
                .param("text", "new comment"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/details/" + book.getId()));

        verify(commentService).insert("new comment", book.getId());
    }

    @Test
    public void addCommentShouldThrowErrorIfValidationFails() throws Exception {
        Book book = getTestBook();
        List<Comment> comments = getTestCommentsFor(book);

        when(bookService.findById(book.getId())).thenReturn(book);
        when(commentService.findByBookId(book.getId())).thenReturn(comments);

        this.mvc.perform(post("/books/" + book.getId() + "/comments")
                        .param("text", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("addCommentDto", "text"))
                .andExpect(model().attributeExists("errors"))
                .andExpect(model().attribute("book", book))
                .andExpect(model().attribute("comments", comments))
                .andExpect(view().name("details"));
    }


    @Test
    public void testConfirmDelete() throws Exception {
        Book book = getTestBook();
        when(bookService.findById(book.getId())).thenReturn(book);
        this.mvc.perform(get("/books/confirm-delete/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("book", book))
                .andExpect(view().name("confirm-delete"));
    }

    @Test
    public void testCreateBook() throws Exception {
        List<Genre> genres = getTestGenres();
        when(genreService.findAll()).thenReturn(genres);
        this.mvc.perform(get("/books/create"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("genres", genres))
                .andExpect(view().name("create"));
    }

    @Test
    public void testSaveBook() throws Exception {
        String authorName = "John";
        String bookTitle = "Test Book";
        Author author = new Author(1, authorName);
        Set<Long> genreIds = Set.of(1L, 2L);

        when(authorService.insert(authorName)).thenReturn(author);


        this.mvc.perform(post("/books/create")
                .param("title", bookTitle)
                .param("authorFullName", authorName)
                .param("genreIds", "1", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        verify(bookService).insert(bookTitle,
                author.getId(),
                genreIds);

    }

    @Test
    public void saveBookShouldThrowErrorIfValidationFails() throws Exception {
        List<Genre> genres = getTestGenres();
        when(genreService.findAll()).thenReturn(genres);

        this.mvc.perform(post("/books/create")
                        .param("title", "")
                        .param("authorFullName", "")
                        .param("genreIds", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeHasFieldErrors("createBookDto",
                        "title", "authorFullName", "genreIds"))
                .andExpect(model().attributeExists("errors"))
                .andExpect(model().attribute("genres", genres))
                .andExpect(view().name("create"));
    }

    private Book getTestBook(){
        Author author = new Author(1L, "John");
        Genre genre = getTestGenres().get(0);
        return new Book(1L, "Test Book", author, List.of(genre));
    }

    private List<Comment> getTestCommentsFor(Book testBook){
        return List.of(new Comment(1, "good", testBook),
                new Comment(2, "bad", testBook));
    }

    private List<Genre> getTestGenres(){
        return List.of(new Genre(1, "Fiction"), new Genre(2, "Fantasy"));
    }
}
