package ru.otus.hw.configs;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.nosql.Author;
import ru.otus.hw.models.nosql.Book;
import ru.otus.hw.models.nosql.Comment;
import ru.otus.hw.models.nosql.Genre;
import ru.otus.hw.models.sql.SourceAuthor;
import ru.otus.hw.models.sql.SourceBook;
import ru.otus.hw.models.sql.SourceComment;
import ru.otus.hw.models.sql.SourceGenre;
import ru.otus.hw.services.CleanUpService;
import ru.otus.hw.services.SqlToNoSqlModelConverterService;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    public static final String IMPORT_BOOK_JOB_NAME = "importBookJob";

    private static final int CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityManagerFactory entityManagerFactory;

    private final CleanUpService cleanUpService;

    private final MongoOperations mongoOperations;

    @Bean
    public ItemReader<SourceBook> bookReader() {
        return new JpaPagingItemReaderBuilder<SourceBook>()
                .name("BookItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from SourceBook b")
                .pageSize(100)
                .build();
    }

    @Bean
    public ItemReader<SourceAuthor> authorReader() {
        return new JpaPagingItemReaderBuilder<SourceAuthor>()
                .name("AuthorItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select a from SourceAuthor a")
                .pageSize(100)
                .build();
    }

    @Bean
    public ItemReader<SourceGenre> genreReader() {
        return new JpaPagingItemReaderBuilder<SourceGenre>()
                .name("GenreItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select g from SourceGenre g")
                .pageSize(100)
                .build();
    }

    @Bean
    public ItemReader<SourceComment> commentReader() {
        return new JpaPagingItemReaderBuilder<SourceComment>()
                .name("CommentItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select c from SourceComment c")
                .pageSize(100)
                .build();
    }

    @Bean
    public ItemProcessor<SourceBook, Book> bookProcessor(SqlToNoSqlModelConverterService converter) {
        return converter::convertBook;
    }

    @Bean
    public ItemProcessor<SourceAuthor, Author> authorProcessor(SqlToNoSqlModelConverterService converter) {
        return converter::convertAuthor;
    }

    @Bean
    public ItemProcessor<SourceGenre, Genre> genreProcessor(SqlToNoSqlModelConverterService converter) {
        return converter::convertGenre;
    }

    @Bean
    public ItemProcessor<SourceComment, Comment> commentProcessor(SqlToNoSqlModelConverterService converter) {
        return converter::convertComment;
    }

    @Bean
    public ItemWriter<Book> bookWriter() {
        return new MongoItemWriterBuilder<Book>()
                .template(mongoOperations)
                .collection("books")
                .build();
    }

    @Bean
    public ItemWriter<Author> authorWriter() {
        return new MongoItemWriterBuilder<Author>()
                .template(mongoOperations)
                .collection("authors")
                .build();
    }

    @Bean
    public ItemWriter<Genre> genreWriter() {
        return new MongoItemWriterBuilder<Genre>()
                .template(mongoOperations)
                .collection("genres")
                .build();
    }

    @Bean
    public ItemWriter<Comment> commentWriter() {
        return new MongoItemWriterBuilder<Comment>()
                .template(mongoOperations)
                .collection("comments")
                .build();
    }

    @Bean
    public MethodInvokingTaskletAdapter cleanUpTasklet() {
        MethodInvokingTaskletAdapter adapter = new MethodInvokingTaskletAdapter();

        adapter.setTargetObject(cleanUpService);
        adapter.setTargetMethod("cleanUp");

        return adapter;
    }

    @Bean
    public Job importBookJob(Flow transformAuthorsAndGenresFlow,
                             Step transformCommentsStep,
                             Step transformBooksStep,
                             Step cleanUpStep) {
        return new JobBuilder(IMPORT_BOOK_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(transformAuthorsAndGenresFlow)
                .next(transformBooksStep)
                .next(transformCommentsStep)
                .next(cleanUpStep)
                .end()
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }

    @Bean
    public Step transformAuthorsStep(ItemReader<SourceAuthor> authorReader, ItemWriter<Author> authorWriter,
                                     ItemProcessor<SourceAuthor, Author> authorProcessor) {
        return new StepBuilder("transformAuthorsStep", jobRepository)
                .<SourceAuthor, Author>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(authorReader)
                .processor(authorProcessor)
                .writer(authorWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Flow transformAuthorsFlow(Step transformAuthorsStep) {
        return new FlowBuilder<Flow>("transformAuthorsFlow")
                .start(transformAuthorsStep)
                .build();
    }

    @Bean
    public Step transformGenresStep(ItemReader<SourceGenre> genreReader, ItemWriter<Genre> genreWriter,
                                    ItemProcessor<SourceGenre, Genre> genreProcessor) {
        return new StepBuilder("transformGenresStep", jobRepository)
                .<SourceGenre, Genre>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(genreReader)
                .processor(genreProcessor)
                .writer(genreWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @Bean
    public Flow transformGenresFlow(Step transformGenresStep) {
        return new FlowBuilder<Flow>("transformGenresFlow")
                .start(transformGenresStep)
                .build();
    }

    @Bean
    public Flow transformAuthorsAndGenresFlow(Flow transformAuthorsFlow, Flow transformGenresFlow) {
        return new FlowBuilder<Flow>("transformAuthorsAndGenresFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(transformAuthorsFlow, transformGenresFlow)
                .build();
    }

    @Bean
    public Step transformCommentsStep(ItemReader<SourceComment> commentReader, ItemWriter<Comment> commentWriter,
                                      ItemProcessor<SourceComment, Comment> commentProcessor) {
        return new StepBuilder("transformCommentsStep", jobRepository)
                .<SourceComment, Comment>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(commentReader)
                .processor(commentProcessor)
                .writer(commentWriter)
                .build();
    }


    @Bean
    public Step transformBooksStep(ItemReader<SourceBook> bookReader, ItemWriter<Book> bookWriter,
                                   ItemProcessor<SourceBook, Book> itemProcessor) {
        return new StepBuilder("transformBooksStep", jobRepository)
                .<SourceBook, Book>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(bookReader)
                .processor(itemProcessor)
                .writer(bookWriter)
                .build();
    }

    @Bean
    public Step cleanUpStep() {
        return new StepBuilder("cleanUpStep", jobRepository)
                .tasklet(cleanUpTasklet(), platformTransactionManager)
                .build();
    }
}
