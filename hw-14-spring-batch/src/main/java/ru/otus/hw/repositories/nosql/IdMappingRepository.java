package ru.otus.hw.repositories.nosql;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.IdMapping;

import java.util.List;
import java.util.Optional;

public interface IdMappingRepository extends JpaRepository<IdMapping, Long> {

    Optional<IdMapping> findByEntityTypeAndSqlId(String entityType, Long sqlId);

    List<IdMapping> findAllByEntityTypeAndSqlIdIn(String entityType, List<Long> sqlIds);
}