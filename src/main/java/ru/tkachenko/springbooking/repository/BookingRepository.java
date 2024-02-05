package ru.tkachenko.springbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tkachenko.springbooking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
