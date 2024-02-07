package ru.tkachenko.springbooking.service;

import org.springframework.data.domain.Pageable;
import ru.tkachenko.springbooking.dto.HotelFilter;
import ru.tkachenko.springbooking.model.Hotel;

import java.util.List;

public interface HotelService {
    List<Hotel> findAll(Pageable pageable, HotelFilter filter);

    Hotel findById(Long id);

    Hotel save(Hotel hotel);

    Hotel update(Hotel hotel);

    void deleteById(Long id);

    Hotel updateRating(Long id, int newMark);

    Long count();
}
