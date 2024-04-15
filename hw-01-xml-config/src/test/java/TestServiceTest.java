import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.haidarov.hw.dao.QuestionDao;
import ru.haidarov.hw.domain.Answer;
import ru.haidarov.hw.domain.Question;
import ru.haidarov.hw.service.IOService;
import ru.haidarov.hw.service.TestServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestServiceTest {

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @InjectMocks
    private TestServiceImpl testService;


    @Test
    public void testExecuteTest() {
        List<Question> questions = Arrays.asList(
                new Question("Q1", Arrays.asList(
                        new Answer("A1", true),
                        new Answer("A2", false))),
                new Question("Q2", Arrays.asList(
                        new Answer("A3", true),
                        new Answer("A4", false)))
        );

        when(questionDao.findAll()).thenReturn(questions);

        testService.executeTest();

        verify(ioService).printLine("");
        verify(ioService).printFormattedLine("Please answer the questions below%n");

        verify(ioService).printLine("Q1");
        verify(ioService).printFormattedLine("%d) A1", 1);
        verify(ioService).printFormattedLine("%d) A2", 2);

        verify(ioService).printLine("Q2");
        verify(ioService).printFormattedLine("%d) A3", 1);
        verify(ioService).printFormattedLine("%d) A4", 2);

        verify(questionDao).findAll();
        verifyNoMoreInteractions(ioService, questionDao);
    }
}
