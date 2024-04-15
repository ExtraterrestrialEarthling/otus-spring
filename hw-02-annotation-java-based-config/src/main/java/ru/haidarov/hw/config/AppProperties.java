package ru.haidarov.hw.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@PropertySource("classpath:application.properties")
@Configuration
public class AppProperties implements AppSettingsProvider {

    private String testFileName;

    private int minPointsToPass;

    public AppProperties(@Value("${app.properties.test-file-name}") String testFileName,
                         @Value("${app.properties.min-points-to-pass}") int minPointsToPass) {
        this.testFileName = testFileName;
        this.minPointsToPass = minPointsToPass;
    }
}
