package ru.haidarov.hw.service;

import lombok.RequiredArgsConstructor;
import ru.haidarov.hw.dao.QuestionDao;
import ru.haidarov.hw.domain.Question;
import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        questions.forEach(question -> {
            ioService.printLine(question.text());
            for (int i = 0; i < question.answers().size(); i++) {
                String answerText = question.answers().get(i).text();
                ioService.printFormattedLine("%d) " + answerText, i + 1);
            }
        });
    }
}
