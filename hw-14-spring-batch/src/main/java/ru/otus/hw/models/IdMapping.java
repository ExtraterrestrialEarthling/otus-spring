package ru.otus.hw.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "id_mappings")
@Getter
@Setter
@NoArgsConstructor
public class IdMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type")
    private String entityType;

    @Column(name = "sql_id")
    private Long sqlId;

    @Column(name = "mongo_id")
    private String mongoId;

    public IdMapping(String entityType, Long sqlId, String mongoId) {
        this.entityType = entityType;
        this.sqlId = sqlId;
        this.mongoId = mongoId;
    }
}
