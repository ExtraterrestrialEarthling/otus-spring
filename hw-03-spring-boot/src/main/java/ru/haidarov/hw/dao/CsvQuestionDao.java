package ru.haidarov.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.haidarov.hw.config.LocaleConfig;
import ru.haidarov.hw.config.TestFileNameProvider;
import ru.haidarov.hw.dao.dto.QuestionDto;
import ru.haidarov.hw.domain.Question;
import ru.haidarov.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    private final LocaleConfig localeConfig;

    @Override
    public List<Question> findAll() {
        List<Question> questions;
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(fileNameProvider.getFileNameByLocaleTag(
                        localeConfig.getLocale().toLanguageTag()));
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
