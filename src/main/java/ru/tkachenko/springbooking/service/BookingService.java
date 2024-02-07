package ru.tkachenko.springbooking.service;

import ru.tkachenko.springbooking.model.Booking;
import ru.tkachenko.springbooking.model.User;

import java.util.List;

public interface BookingService {
    List<Booking> findAll();
    Booking save(User user, Booking booking);

}
