package ru.tkachenko.springbooking.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tkachenko.springbooking.model.Hotel;
import ru.tkachenko.springbooking.repository.HotelRepository;

import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "app.database.init", name = "enabled", havingValue = "true")
public class InitDatabase {
    private final HotelRepository hotelRepository;

    @PostConstruct
    public void initData() {
        if (hotelRepository.count() > 0) {
            log.info("В базе уже есть записи!");
            return;
        }

        int countHotels = 5;

        for (int i = 1; i <= countHotels; i++) {
            Hotel hotel = Hotel.builder()
                    .name("Hotel_" + i)
                    .title("Title Hotel " + i)
                    .city(i % 2 == 0 ? "City_1" : "City_2")
                    .address("Address_" + i)
                    .distanceFromCityCenter(0.5 + (5 - 0.5) * new Random().nextDouble())
                    .rating(0.5 + (5 - 0.5) * new Random().nextDouble())
                    .numberOfRatings(new Random().nextInt((100 - 5) + 1) + 5)
                    .build();
            hotelRepository.save(hotel);
        }
    }
}
