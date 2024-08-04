package ru.otus.hw.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.hw.dto.request.BookDtoRq;
import ru.otus.hw.dto.response.AuthorDtoRs;
import ru.otus.hw.dto.response.BookDtoRs;
import ru.otus.hw.dto.response.GenreDtoRs;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;


import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Long NON_EXISTING_ID = 999L;

    private ObjectMapper om = new ObjectMapper();
    ;

    @BeforeEach
    public void setUp() {
        when(bookService.findById(1)).thenReturn(getTestBook1());
        when(bookService.findAll()).thenReturn(getAllTestBooks());
        when(bookService.update(any())).thenReturn(getTestBook2());
        when(bookService.insert(any())).thenReturn(getTestBook1());
        when(bookService.findById(NON_EXISTING_ID)).thenThrow(new EntityNotFoundException("not found"));
    }


    @Test
    public void testGetBookById() throws Exception {
        String expectedJson = om.writeValueAsString(getTestBook1());
        MvcResult result = mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();

        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }

    @Test
    public void testGetAllBooks() throws Exception {
        String expectedJson = om.writeValueAsString(getAllTestBooks());

        MvcResult result = mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();

        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }

    @Test
    public void testUpdateBook() throws Exception {
        String expectedJson = om.writeValueAsString(getTestBook2());

        String request = om.writeValueAsString(
                new BookDtoRq(2L, "NewTitle", 2L, Set.of(2L, 3L)));

        MvcResult result = mockMvc.perform(put("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();

        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isOk())
                .andReturn();
        verify(bookService).deleteById(1);
    }

    @Test
    public void testCreateBook() throws Exception {
        String expectedJson = om.writeValueAsString(getTestBook1());

        String request = om.writeValueAsString(
                new BookDtoRq(1L, "NewBook", 1L, Set.of(1L, 2L)));

        MvcResult result = mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().is(201))
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();

        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }

    @Test
    public void FindByIdShouldThrowExceptionIfIdNotExist() throws Exception {
        mockMvc.perform(get("/api/books/" + NON_EXISTING_ID))
                .andExpect(status().is(404))
                .andReturn();
    }


    private BookDtoRs getTestBook1() {
        return new BookDtoRs(1L, "TestBook",
                new AuthorDtoRs(1L, "TestAuthor"),
                Arrays.asList(new GenreDtoRs(1L, "TestGenre1"),
                        new GenreDtoRs(2L, "TestGenre2")));
    }

    private BookDtoRs getTestBook2() {
        return new BookDtoRs(2L, "TestBook2",
                new AuthorDtoRs(2L, "TestAuthor2"),
                Arrays.asList(new GenreDtoRs(3L, "TestGenre3"),
                        new GenreDtoRs(4L, "TestGenre4")));
    }

    private List<BookDtoRs> getAllTestBooks() {
        return List.of(getTestBook1(), getTestBook2());
    }
}
