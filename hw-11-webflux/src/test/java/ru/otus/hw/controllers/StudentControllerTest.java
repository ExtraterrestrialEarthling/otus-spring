package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import ru.otus.hw.dto.request.StudentDtoRq;
import ru.otus.hw.dto.response.StudentDtoRs;
import ru.otus.hw.dto.response.TeacherDtoRs;
import ru.otus.hw.dto.response.SkillDtoRs;
import ru.otus.hw.mapper.StudentMapper;
import ru.otus.hw.models.Skill;
import ru.otus.hw.models.Student;
import ru.otus.hw.models.Teacher;
import ru.otus.hw.repositories.SkillRepository;
import ru.otus.hw.repositories.StudentRepository;
import ru.otus.hw.repositories.TeacherRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private SkillRepository skillRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    @MockBean
    private StudentMapper studentMapper;

    private Student student;
    private Teacher teacher;
    private List<Skill> skills;
    private StudentDtoRs studentDtoRs;
    private StudentDtoRq studentDtoRq;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        teacher = new Teacher("1", "Dr. John Smith");
        skills = Arrays.asList(new Skill("1", "Java"), new Skill("2", "Spring"));
        student = new Student("123", "Alice Johnson", teacher, skills);

        studentDtoRq = new StudentDtoRq("123", "Alice Johnson", "1", Set.of("1", "2"));
        studentDtoRs = new StudentDtoRs(
                "123",
                "Alice Johnson",
                new TeacherDtoRs("1", "Dr. John Smith"),
                Arrays.asList(new SkillDtoRs("1", "Java"), new SkillDtoRs("2", "Spring"))
        );
    }

    @Test
    void getStudentById_ShouldReturnStudent() {
        when(studentRepository.findById("123")).thenReturn(Mono.just(student));
        when(studentMapper.entityToDtoRs(student)).thenReturn(studentDtoRs);

        webTestClient.get()
                .uri("/api/students/123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(StudentDtoRs.class)
                .isEqualTo(studentDtoRs);
    }

    @Test
    void getStudentById_ShouldReturnNotFound() {
        when(studentRepository.findById("123")).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/api/students/123")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getAllStudents_ShouldReturnListOfStudents() {
        when(studentRepository.findAll()).thenReturn(Flux.just(student));
        when(studentMapper.entityToDtoRs(student)).thenReturn(studentDtoRs);

        webTestClient.get()
                .uri("/api/students")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StudentDtoRs.class)
                .contains(studentDtoRs);
    }

    @Test
    void createStudent_ShouldCreateStudent() {
        when(teacherRepository.findById("1")).thenReturn(Mono.just(teacher));
        when(skillRepository.findByIdIn(studentDtoRq.getSkillIds())).thenReturn(Flux.fromIterable(skills));
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.just(student));
        when(studentMapper.entityToDtoRs(student)).thenReturn(studentDtoRs);

        webTestClient.post()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDtoRq)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDtoRs.class)
                .isEqualTo(studentDtoRs);
    }

    @Test
    void updateStudent_ShouldUpdateStudent() {
        when(teacherRepository.findById("1")).thenReturn(Mono.just(teacher));
        when(skillRepository.findByIdIn(studentDtoRq.getSkillIds())).thenReturn(Flux.fromIterable(skills));
        when(studentRepository.save(any(Student.class))).thenReturn(Mono.just(student));
        when(studentMapper.entityToDtoRs(student)).thenReturn(studentDtoRs);

        webTestClient.put()
                .uri("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(studentDtoRq)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentDtoRs.class)
                .isEqualTo(studentDtoRs);
    }

    @Test
    void deleteStudentById_ShouldDeleteStudent() {
        when(studentRepository.deleteById("123")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/api/students/123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Successfully deleted student with id 123");
    }
}
