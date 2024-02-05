package ru.tkachenko.springbooking.service;

import ru.tkachenko.springbooking.model.Booking;

import java.util.List;

public interface BookingService {
    List<Booking> findAll();
    Booking save(Booking booking);

}
