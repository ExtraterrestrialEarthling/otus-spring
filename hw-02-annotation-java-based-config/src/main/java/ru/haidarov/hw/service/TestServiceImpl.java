package ru.haidarov.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.haidarov.hw.config.AppSettingsProvider;
import ru.haidarov.hw.dao.QuestionDao;
import ru.haidarov.hw.domain.Question;
import ru.haidarov.hw.domain.User;
import ru.haidarov.hw.exceptions.InputReadException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final AppSettingsProvider appSettingsProvider;

    private final IOService ioService;

    private final QuestionDao questionDao;

    private int points;

    @Override
    public void executeTest() {
        User user = createUserFromInput();
        points = 0;
        ioService.printFormattedLine("Please answer the questions below%n");
        List<Question> questions = questionDao.findAll();
        questions.forEach(question -> {
            displayQuestionAndAnswers(question);
            String userAnswer = ioService.getUserInput();
            if(!isValidUserInput(userAnswer, questions.size())){
                throw new InputReadException("Input is not valid.");
            }
            incrementPointsIfCorrectAnswer(question, userAnswer);
        });
        displayTestResult(points, questions.size());
    }

    private void displayQuestionAndAnswers(Question question){
        ioService.printLine(question.text());
        for (int i = 0; i < question.answers().size(); i++) {
            String answerText = question.answers().get(i).text();
            ioService.printFormattedLine("%d) " + answerText, i + 1);
        }
    }

    private void displayTestResult(int userScore, int maxScore){
        if (points >= appSettingsProvider.getMinPointsToPass()){
            ioService.printFormattedLine("Congratulations! You passed the test. " +
                    "Your score: %d out of %d.", userScore, maxScore);
        } else{
            ioService.printFormattedLine("Sorry, you failed. " +
                    "Your score: %d out of %d. Try again.", userScore, maxScore);
        }
    }

    private User createUserFromInput(){
        ioService.printLine("Enter your first name.");
        String firstName = ioService.getUserInput();
        ioService.printLine("Enter your last name.");
        String lastName = ioService.getUserInput();
        return new User(firstName, lastName);
    }

    private void incrementPointsIfCorrectAnswer(Question question, String userAnswer){
        if(question.answers().get(Integer.parseInt(userAnswer)-1).isCorrect()){
            points++;
        }
    }

    private boolean isValidUserInput(String userInput, int maxNumberOfOptions){
        int userAnswer;
        try{
            userAnswer = Integer.parseInt(userInput);
        } catch (Exception e){
            return false;
        }
        return userAnswer >= 1 && userAnswer <= maxNumberOfOptions;
    }
}
