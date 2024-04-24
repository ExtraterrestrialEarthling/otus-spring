package ru.haidarov.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@ConfigurationProperties(prefix = "logging.execution-time")
public class ExecutionTimeLoggerProperties {

    @Getter
    private boolean enabled;

}
