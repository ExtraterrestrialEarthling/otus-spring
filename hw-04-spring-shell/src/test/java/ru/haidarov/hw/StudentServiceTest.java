package ru.haidarov.hw;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.haidarov.hw.domain.Student;
import ru.haidarov.hw.service.LocalizedIOService;
import ru.haidarov.hw.service.LocalizedIOServiceImpl;
import ru.haidarov.hw.service.StudentServiceImpl;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = {StudentServiceImpl.class, LocalizedIOServiceImpl.class},
        properties = {"spring.shell.interactive.enabled=false"})
public class StudentServiceTest {

    @Autowired
    StudentServiceImpl studentService;

    @MockBean
    LocalizedIOService localizedIOService;

    @Test
    public void nameShouldBeCorrect(){
        when(localizedIOService.readStringWithPromptLocalized("StudentService.input.first.name")).thenReturn("John");
        when(localizedIOService.readStringWithPromptLocalized("StudentService.input.last.name")).thenReturn("Smith");
        Assertions.assertThat(studentService.determineCurrentStudent()).isEqualTo(
                        new Student("John", "Smith"));
    }

}
