package ru.haidarov.hw.dao;

import ru.haidarov.hw.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
