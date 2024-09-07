package ru.otus.hw.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDtoRs {

    private Long id;

    private String title;

    private AuthorDtoRs author;

    private List<GenreDtoRs> genres;
}
