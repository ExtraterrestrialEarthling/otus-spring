package ru.haidarov.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.haidarov.hw.config.AppSettingsProvider;
import ru.haidarov.hw.dao.dto.QuestionDto;
import ru.haidarov.hw.domain.Question;
import ru.haidarov.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final AppSettingsProvider appSettingsProvider;

    @Override
    public List<Question> findAll() {
        List<Question> questions;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(appSettingsProvider.getTestFileName());
        try {
            questions = new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(inputStream))
                    .withSkipLines(1)
                    .withSeparator(';')
                    .withType(QuestionDto.class)
                    .build()
                    .parse()
                    .stream()
                    .map(QuestionDto::toDomainObject)
                    .toList();
        } catch (Exception e) {
            throw new QuestionReadException("Error occurred while reading questions", e);
        }
        return questions;
    }
}
