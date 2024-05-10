package ru.haidarov.hw;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import ru.haidarov.hw.config.TestFileNameProvider;
import ru.haidarov.hw.service.IOService;

@TestConfiguration
public class IntegrationTestConfig {

    @Bean
    @Primary
    public TestFileNameProvider testFileNameProvider() {
        return new TestFileNameProvider() {
            @Override
            public String getTestFileName() {
                return "questions-test.csv";
            }
            @Override
            public String getFileNameByLocaleTag(String locale) {
                return "questions-test.csv";
            }
        };
    }

    @Bean
    @Primary
    public IOService ioService() {
        return new IOService() {
            @Override
            public void printLine(String s) {
            }

            @Override
            public void printFormattedLine(String s, Object... args) {
            }

            @Override
            public String readString() {
                return "John";
            }

            @Override
            public String readStringWithPrompt(String prompt) {
                return "John";
            }

            @Override
            public int readIntForRange(int min, int max, String errorMessage) {
                return 1;
            }

            @Override
            public int readIntForRangeWithPrompt(int min, int max, String prompt, String errorMessage) {
                return 1;
            }
        };
    }
}