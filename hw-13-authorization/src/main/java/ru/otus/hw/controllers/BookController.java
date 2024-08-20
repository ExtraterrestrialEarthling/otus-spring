package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.otus.hw.dto.AddCommentDto;
import ru.otus.hw.dto.CreateBookDto;
import ru.otus.hw.dto.EditBookDto;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    private final CommentService commentService;

    private final GenreService genreService;

    private final AuthorService authorService;

    @GetMapping()
    public String listBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "list";
    }

    @GetMapping("/details/{bookId}")
    public String getBookDetailsByBookId(@PathVariable long bookId, Model model) {
        Book book = bookService.findById(bookId);
        List<Comment> comments = commentService.findByBookId(bookId);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "details";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "edit";
    }

    @PostMapping("/edit")
    public String applyChanges(@Valid EditBookDto editBookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Book book = bookService.findById(editBookDto.getBookId());
            List<Genre> genres = genreService.findAll();
            model.addAttribute("book", book);
            model.addAttribute("genres", genres);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "edit";
        }
        bookService.update(
                editBookDto.getBookId(),
                editBookDto.getTitle(),
                editBookDto.getAuthorId(),
                editBookDto.getGenreIds(),
                editBookDto.isAdultOnly());
        authorService.update(
                editBookDto.getAuthorId(),
                editBookDto.getAuthorFullName());
        return "redirect:/books/details/" + editBookDto.getBookId();
    }

    @PostMapping("/delete/{id}")
    public String deleteBook(@PathVariable long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }


    @PostMapping("{bookId}/comments")
    public String addComment(@PathVariable long bookId,
                             @Valid AddCommentDto addCommentDto,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            Book book = bookService.findById(bookId);
            List<Comment> comments = commentService.findByBookId(bookId);
            model.addAttribute("book", book);
            model.addAttribute("comments", comments);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "details";
        }
        commentService.insert(addCommentDto.getText(), bookId);
        return "redirect:/books/details/" + bookId;
    }

    @GetMapping("/confirm-delete/{id}")
    public String confirmDelete(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        model.addAttribute("book", book);
        return "confirm-delete";
    }

    @GetMapping("/create")
    public String createBook(Model model) {
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "create";
    }

    @PostMapping("/create")
    public String saveBook(@Valid CreateBookDto createBookDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Genre> genres = genreService.findAll();
            model.addAttribute("genres", genres);
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "create";
        }
        Author author = authorService.insert(createBookDto.getAuthorFullName());
        bookService.insert(
                createBookDto.getTitle(),
                author.getId(),
                createBookDto.getGenreIds(),
                createBookDto.isAdultOnly()
        );
        return "redirect:/books";
    }
}


