package ru.tkachenko.springbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springbooking.model.Booking;
import ru.tkachenko.springbooking.repository.BookingRepository;
import ru.tkachenko.springbooking.service.BookingService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseBookingService implements BookingService {
    private final BookingRepository repository;

    @Override
    public List<Booking> findAll() {
        return repository.findAll();
    }

    @Override
    public Booking save(Booking booking) {
        return repository.save(booking);
    }
}
