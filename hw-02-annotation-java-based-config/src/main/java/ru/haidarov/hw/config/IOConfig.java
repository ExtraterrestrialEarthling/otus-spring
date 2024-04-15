package ru.haidarov.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.io.PrintStream;

@Configuration
public class IOConfig {

    @Bean
    public PrintStream printStream(){
        return System.out;
    }

    @Bean
    public InputStream inputStream(){
        return System.in;
    }
}
