package ru.haidarov.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.haidarov.hw.dao.QuestionDao;
import ru.haidarov.hw.domain.Question;
import ru.haidarov.hw.domain.Student;
import ru.haidarov.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question : questions) {
            String prompt = toQuestionWithEnumeratedOptionsString(question);
            ioService.printLine(prompt);
            int answer = ioService.readIntForRangeLocalized(1, question.answers().size(), "TestService.incorrect.input");
            boolean isAnswerValid = question.answers().get(answer - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

    private String toQuestionWithEnumeratedOptionsString(Question question) {
        StringBuilder sb = new StringBuilder(question.text() + "\n");
        for (int i = 0; i < question.answers().size(); i++) {
            sb.append(i + 1).append(") ").append(question.answers().get(i).text()).append("\n");
        }
        return sb.toString();
    }
}
