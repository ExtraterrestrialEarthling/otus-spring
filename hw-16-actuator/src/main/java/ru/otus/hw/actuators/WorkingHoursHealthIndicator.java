package ru.otus.hw.actuators;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class WorkingHoursHealthIndicator implements HealthIndicator {

    @Value("${working-hours.start}")
    private String startOfWork;

    @Value("${working-hours.end}")
    private String endOfWork;

    public Health health() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startOfWork, formatter);
        LocalTime endTime = LocalTime.parse(endOfWork, formatter);

        LocalTime time = LocalTime.now();

        if (time.isAfter(startTime) && time.isBefore(endTime)) {
            return Health.up()
                    .withDetail("Working hours", "Library is working, please, come get books")
                    .build();
        }
        return Health.down()
                .withDetail("Working hours", "All the librarians are sleeping, please, come tomorrow")
                .build();
    }
}
