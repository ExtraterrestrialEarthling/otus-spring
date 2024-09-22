package ru.otus.hw.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDtoRq {

    private Long id;

    @NotEmpty(message = "Author's name should not be empty")
    private String fullName;
}
