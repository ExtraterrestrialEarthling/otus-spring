package ru.otus.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.models.nosql.Book;
import ru.otus.hw.models.sql.SourceBook;
import ru.otus.hw.models.sql.SourceGenre;
import ru.otus.hw.repositories.nosql.BookRepository;
import ru.otus.hw.repositories.sql.SourceBookRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBatchTest
@SpringBootTest(properties = {"spring.shell.interactive.enabled=false"})
@EnableAutoConfiguration(exclude = {
        org.springframework.shell.boot.StandardCommandsAutoConfiguration.class
})
public class BatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private SourceBookRepository sourceBookRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    public void testTransformBookStep() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("transformBooksStep");

        assertThat(jobExecution.getStepExecutions().iterator().next().getStatus()).isEqualTo(BatchStatus.COMPLETED);

        List<SourceBook> sourceBooks = sourceBookRepository.findAll();

        for (SourceBook sourceBook : sourceBooks) {
            Optional<Book> optionalBook = bookRepository.findById(String.valueOf(sourceBook.getId()));
            assertThat(optionalBook).isPresent();

            Book book = optionalBook.get();

            assertThat(book.getTitle()).isEqualTo(sourceBook.getTitle());
            assertThat(book.getAuthor().getFullName()).isEqualTo(sourceBook.getAuthor().getFullName());
            assertThat(book.getGenres().size()).isEqualTo(sourceBook.getGenres().size());
            assertThat(book.getGenres()).extracting("name").containsExactlyInAnyOrderElementsOf(
                    sourceBook.getGenres().stream().map(SourceGenre::getName).toList()
            );
        }
    }
}
