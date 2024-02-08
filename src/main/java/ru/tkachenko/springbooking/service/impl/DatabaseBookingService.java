package ru.tkachenko.springbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springbooking.exception.DateException;
import ru.tkachenko.springbooking.model.Booking;
import ru.tkachenko.springbooking.model.Room;
import ru.tkachenko.springbooking.model.UnavailableDate;
import ru.tkachenko.springbooking.model.User;
import ru.tkachenko.springbooking.repository.BookingRepository;
import ru.tkachenko.springbooking.repository.UnavailableDateRepository;
import ru.tkachenko.springbooking.service.BookingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseBookingService implements BookingService {
    private final BookingRepository repository;
    private final UnavailableDateRepository unavailableDateRepository;

    @Override
    public List<Booking> findAll() {
        return repository.findAll();
    }

    @Override
    public Booking save(User user, Booking booking) {

        if (booking.getArrivalDate().isAfter(booking.getDepartureDate())) {
            throw new DateException("Дата заезда не может быть позже даты выезда!");
        }

        booking.setUser(user);

        Room room = booking.getRoom();

        List<LocalDate> existingUnavailableDates = room.getUnavailableDates()
                .stream().map(UnavailableDate::getDate).toList();

        boolean datesUnavailable = existingUnavailableDates.stream().anyMatch(
                date -> !date.isBefore(booking.getArrivalDate()) && !date.isAfter(booking.getDepartureDate())
        );

        if (datesUnavailable) {
            throw new DateException("Комната на Ваши даты забронирована!");
        }

        List<UnavailableDate> unavailableDate =
                createUnavailableDates(booking.getArrivalDate(), booking.getDepartureDate(), room);

        unavailableDateRepository.saveAll(unavailableDate);

        return repository.save(booking);
    }

    private List<UnavailableDate> createUnavailableDates(LocalDate from, LocalDate to, Room room) {
        List<UnavailableDate> result = new ArrayList<>();
        LocalDate currentDate = from;
        while (!currentDate.isAfter(to)) {
            UnavailableDate unavailableDate = new UnavailableDate();
            unavailableDate.setRoom(room);
            unavailableDate.setDate(currentDate);
            result.add(unavailableDate);
            currentDate = currentDate.plusDays(1);
        }
        return result;
    }
}
