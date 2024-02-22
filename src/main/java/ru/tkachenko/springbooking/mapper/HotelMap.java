package ru.tkachenko.springbooking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tkachenko.springbooking.model.Hotel;
import ru.tkachenko.springbooking.service.HotelService;

@Component
@RequiredArgsConstructor
public class HotelMap {
    private final HotelService hotelService;

    public Hotel fromId(Long id) {
        return id != null ? hotelService.findById(id) : null;
    }
}
