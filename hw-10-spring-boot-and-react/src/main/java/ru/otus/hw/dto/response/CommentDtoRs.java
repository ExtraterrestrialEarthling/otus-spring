package ru.otus.hw.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoRs {

    private Long id;

    private String text;

    private Long bookId;
}
