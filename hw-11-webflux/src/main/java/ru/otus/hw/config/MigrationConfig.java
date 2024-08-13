package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.hw.models.Skill;
import ru.otus.hw.models.Student;
import ru.otus.hw.models.Teacher;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class MigrationConfig {

    private final ReactiveMongoTemplate mongoTemplate;

    private final Logger logger = LoggerFactory.getLogger(MigrationConfig.class);

    @Bean
    public CommandLineRunner migrateDatabase() {
        return args -> {
            clearDatabase()
                    .then(insertInitialData())
                    .subscribeOn(Schedulers.boundedElastic())
                    .block();
        };
    }

    private Mono<Void> clearDatabase() {
        return mongoTemplate.getCollectionNames()
                .flatMap(collectionName -> mongoTemplate.dropCollection(collectionName)
                        .then(Mono.just(collectionName)))
                .doOnNext(name -> logger.info("Dropped collection: " + name))
                .doOnError(e -> logger.warn("Error clearing collection: " + e.getMessage()))
                .then(Mono.fromRunnable(() -> logger.info("Database cleared")));
    }

    private Mono<Void> insertInitialData() {
        Teacher teacher = new Teacher(null, "Dr. John Smith");
        List<Skill> skills = Arrays.asList(
                new Skill(null, "Java"),
                new Skill(null, "Spring")
        );

        return mongoTemplate.insertAll(Arrays.asList(teacher, skills.get(0), skills.get(1)))
                .thenMany(insertStudents(teacher, skills))
                .then();
    }

    private Mono<Void> insertStudents(Teacher teacher, List<Skill> skills) {
        List<Student> students = Arrays.asList(
                new Student(null, "Alice Johnson", teacher, skills),
                new Student(null, "Bob Brown", teacher, skills),
                new Student(null, "Charlie Davis", teacher, skills)
        );

        return mongoTemplate.insertAll(students)
                .doOnNext(student -> logger.info("Inserted student: " + student.getName()))
                .then(Mono.fromRunnable(() -> logger.info("Students inserted")));
    }
}
