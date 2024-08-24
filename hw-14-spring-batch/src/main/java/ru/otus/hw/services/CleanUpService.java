package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.repositories.nosql.IdMappingRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CleanUpService {

    private final IdMappingRepository idMappingRepository;

    @SuppressWarnings("unused")
    public void cleanUp() throws Exception {
        log.info("Выполняю завершающие мероприятия...");
        idMappingRepository.deleteAll();
        log.info("Завершающие мероприятия закончены");
    }
}
