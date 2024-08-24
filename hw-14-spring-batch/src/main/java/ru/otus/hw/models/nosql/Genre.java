package ru.otus.hw.models.nosql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("genres")
public class Genre {
    @Id
    private String id;

    private String name;

    public Genre(String id) {
        this.id = id;
    }
}
