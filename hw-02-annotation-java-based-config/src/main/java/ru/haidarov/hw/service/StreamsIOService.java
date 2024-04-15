package ru.haidarov.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.haidarov.hw.exceptions.InputReadException;

import java.io.*;

@Service
@RequiredArgsConstructor
public class StreamsIOService implements IOService {

    private final PrintStream printStream;

    private final InputStream inputStream;

    @Override
    public void printLine(String s) {
        printStream.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        printStream.printf(s + "%n", args);
    }

    @Override
    public String getUserInput() {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        try{
            return bufferedReader.readLine();
        } catch (IOException e) {
            throw new InputReadException("Error reading user input", e);
        }
    }
}
