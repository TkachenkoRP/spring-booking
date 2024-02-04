package ru.tkachenko.springbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tkachenko.springbooking.model.UnavailableDate;

public interface UnavailableDateRepository extends JpaRepository<UnavailableDate, Long> {
}
