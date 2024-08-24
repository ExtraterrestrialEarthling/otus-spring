package ru.otus.hw.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class EditBookDto {

    @NotNull
    private Long bookId;

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotNull
    private Long authorId;

    @NotEmpty(message = "Author cannot be empty")
    private String authorFullName;

    @NotEmpty(message = "At least one genre must be selected.")
    private Set<Long> genreIds;

    private boolean adultOnly;
}
