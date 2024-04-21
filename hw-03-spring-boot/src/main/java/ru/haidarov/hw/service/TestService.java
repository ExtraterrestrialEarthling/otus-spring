package ru.haidarov.hw.service;

import ru.haidarov.hw.domain.Student;
import ru.haidarov.hw.domain.TestResult;

public interface TestService {
    TestResult executeTestFor(Student student);
}
