package ru.otus.hw.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRq {

    private Long id;

    @NotEmpty(message = "Comment text should not be empty")
    private String text;

    @NotNull(message = "Comment should refer to a book")
    private Long bookId;
}
