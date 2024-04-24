package ru.haidarov.hw;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.haidarov.hw.dao.CsvQuestionDao;
import ru.haidarov.hw.domain.Question;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = IntegrationTestConfig.class)
public class CsvQuestionDaoIntegrationTest {

    @Autowired
    private CsvQuestionDao csvQuestionDao;

    @Test
    public void findAllShouldReturnNonEmptyList() {
        List<Question> questions = csvQuestionDao.findAll();
        assertThat(questions).isNotEmpty();
    }

    @Test
    public void testQuestionCount() {
        List<Question> questions = csvQuestionDao.findAll();
        assertThat(questions).hasSize(5);
    }

    @Test
    public void findAllShouldSkipTheFirstLine() {
        List<Question> questions = csvQuestionDao.findAll();
        assertThat(questions.get(0).text()).isEqualTo("Is there life on Mars?");
    }
}