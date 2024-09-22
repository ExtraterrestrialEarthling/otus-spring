package ru.otus.hw.domain;

import lombok.Data;

@Data
public class CarVo {

    private String brand;

    private String model;

    private int year;

    private String color;

    private double pricePerHour;

    private int mileage;

    private String status;
}
