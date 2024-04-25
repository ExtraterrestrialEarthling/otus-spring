package ru.haidarov.hw.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.haidarov.hw.logging.ExecutionTimeLoggerProperties;

@EnableConfigurationProperties({AppProperties.class, ExecutionTimeLoggerProperties.class})
@Configuration
public class AppConfig {
}
