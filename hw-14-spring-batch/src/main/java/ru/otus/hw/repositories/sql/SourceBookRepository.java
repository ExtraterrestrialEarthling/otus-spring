package ru.otus.hw.repositories.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.sql.SourceBook;

public interface SourceBookRepository extends JpaRepository<SourceBook, Long> {
}
