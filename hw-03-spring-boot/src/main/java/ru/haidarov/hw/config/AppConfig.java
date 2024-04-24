package ru.haidarov.hw.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties({AppProperties.class, ExecutionTimeLoggerProperties.class})
@Configuration
public class AppConfig {
}
