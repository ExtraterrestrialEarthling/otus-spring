package ru.haidarov.hw.config;

public interface TestFileNameProvider {
    String getTestFileName();

    String getFileNameByLocaleTag(String locale);
}
