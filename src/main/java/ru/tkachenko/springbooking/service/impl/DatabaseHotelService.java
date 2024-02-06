package ru.tkachenko.springbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.tkachenko.springbooking.exception.EntityNotFoundException;
import ru.tkachenko.springbooking.exception.HotelException;
import ru.tkachenko.springbooking.model.Hotel;
import ru.tkachenko.springbooking.repository.HotelRepository;
import ru.tkachenko.springbooking.service.HotelService;
import ru.tkachenko.springbooking.utils.BeanUtils;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseHotelService implements HotelService {
    private final HotelRepository repository;

    @Override
    public List<Hotel> findAll(Pageable pageable) {
        return repository.findAll(pageable).getContent();
    }

    @Override
    public Hotel findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Отель с ID {0} не найден!", id
                )));
    }

    @Override
    public Hotel save(Hotel hotel) {
        return repository.save(hotel);
    }

    @Override
    public Hotel update(Hotel hotel) {
        Hotel existedHotel = findById(hotel.getId());
        BeanUtils.copyNonNullProperties(hotel, existedHotel);
        return repository.save(existedHotel);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Hotel updateRating(Long id, int newMark) {

        if (newMark < 1 || newMark > 5) {
            throw new HotelException("Недопустимая оценка. Оценка должна быть между 1 и 5.");
        }

        Hotel existedHotel = findById(id);

        double rating = existedHotel.getRating();
        int numberOfRating = existedHotel.getNumberOfRatings();

        double totalRating = rating * numberOfRating;

        totalRating = totalRating - rating + newMark;

        rating = Math.round((totalRating / numberOfRating) * 100.0) / 100.0;

        numberOfRating = numberOfRating + 1;

        existedHotel.setRating(rating);
        existedHotel.setNumberOfRatings(numberOfRating);

        return repository.save(existedHotel);
    }
}
