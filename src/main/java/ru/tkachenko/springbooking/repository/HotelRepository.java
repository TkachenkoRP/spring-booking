package ru.tkachenko.springbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tkachenko.springbooking.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
