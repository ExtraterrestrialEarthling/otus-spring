package ru.otus.hw.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.domain.CarVo;

@FeignClient(name = "car-service")
public interface CarServiceProxy {

    @GetMapping("/api/cars/{id}")
    CarVo findCarById(@PathVariable Long id);
}
