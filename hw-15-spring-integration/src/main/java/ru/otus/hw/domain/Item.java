package ru.otus.hw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Item implements Serializable {

    private String name;

    private BigDecimal price;
}
