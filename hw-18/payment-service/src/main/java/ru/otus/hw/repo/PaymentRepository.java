package ru.otus.hw.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
