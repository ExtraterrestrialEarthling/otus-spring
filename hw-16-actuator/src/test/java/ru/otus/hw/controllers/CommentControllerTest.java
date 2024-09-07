package ru.otus.hw.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.dto.response.CommentDtoRs;
import ru.otus.hw.services.CommentService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private final ObjectMapper om = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        when(commentService.findByBookId(1)).thenReturn(getTestComments());
    }

    @Test
    public void testFindByBookId() throws Exception {
        String expectedJson = om.writeValueAsString(getTestComments());
        var result = mockMvc.perform(get("/api/comments")
                        .param("bookId", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String actualJson = result.getResponse().getContentAsString();

        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }

    private List<CommentDtoRs> getTestComments() {
        return List.of(new CommentDtoRs(1L, "good book", 1L),
                new CommentDtoRs(2L, "bad book", 1L));
    }
}
