package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.security.CustomUserDetailsService;
import ru.otus.hw.security.SecurityConfiguration;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest
@Import({SecurityConfiguration.class})
public class ControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private final List<String> userGetEndpoints = List.of("/books",
            "/books/details/1");

    private final List<String> adminGetEndpoints = List.of("/books/edit/1",
            "/books/confirm-delete/1", "/books/create");

    private final List<String> postEndpoints = List.of(
            "/books/edit",
            "/books/delete/1",
            "/books/1/comments",
            "/books/create");


    @BeforeEach
    public void setUp() {
        Genre mockGenre = new Genre(1L, "");
        Author mockAuthor = new Author(1L, "");
        Book mockBook = new Book(1L, "", mockAuthor, List.of(mockGenre), false);
        Comment mockComment = new Comment(1L, "", mockBook);

        when(bookService.findAll()).thenReturn(List.of(mockBook));
        when(bookService.findById(1)).thenReturn(mockBook);
        when(authorService.findAll()).thenReturn(List.of(mockAuthor));
        when(commentService.findByBookId(1)).thenReturn(List.of(mockComment));
        when(genreService.findAll()).thenReturn(List.of(mockGenre));
    }

    @Test
    public void testUserGetEndpointsAuthorized() throws Exception {

        for (String endpoint : userGetEndpoints) {
            mockMvc.perform(get(endpoint)
                            .with(user("user").authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }


    @Test
    public void testAdminGetEndpointsAuthorized() throws Exception {
        for (String endpoint : userGetEndpoints) {
            mockMvc.perform(get(endpoint)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

    }

    @Test
    public void testAllGetEndpointsUnauthorized() throws Exception {
        List<String> allEndpoints = new ArrayList<>(userGetEndpoints);
        allEndpoints.addAll(adminGetEndpoints);
        for (String endpoint : allEndpoints) {
            mockMvc.perform(get(endpoint))
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }
    }

    @Test
    public void testAllPostEndpointsUnauthorized() throws Exception {
        for (String endpoint : postEndpoints) {
            mockMvc.perform(post(endpoint))
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(redirectedUrl("http://localhost/login"));
        }
    }
}
