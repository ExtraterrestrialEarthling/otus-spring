package ru.haidarov.hw.domain;

import java.util.List;

public record Question(String text, List<Answer> answers) {
}
