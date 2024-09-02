package ru.otus.hw.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoRq {

    private Long bookId;

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Book should have an author")
    private Long authorId;

    @NotEmpty(message = "At least one genre must be selected.")
    private Set<Long> genreIds;
}
