package ru.tkachenko.springbooking.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ru.tkachenko.springbooking.model.*;
import ru.tkachenko.springbooking.repository.HotelRepository;
import ru.tkachenko.springbooking.repository.RoomRepository;
import ru.tkachenko.springbooking.repository.UnavailableDateRepository;
import ru.tkachenko.springbooking.repository.UserRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "app.database.init", name = "enabled", havingValue = "true")
public class InitDatabase {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final UnavailableDateRepository unavailableDateRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void initData() {
        if (hotelRepository.count() > 0) {
            log.info("В базе уже есть записи!");
            return;
        }

        int countHotels = 5;
        int countRoomsInHotel = 7;
        int countUser = 5;

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
            hotel = hotelRepository.save(hotel);

            for (int j = 1; j <= countRoomsInHotel; j++) {
                Room room = Room.builder()
                        .name("RoomName_" + i + j)
                        .description("RoomDescription_" + i + j)
                        .number(j)
                        .price(1000 + (5000 - 1000) * new Random().nextDouble())
                        .capacity((byte) (new Random().nextInt((5 - 1) + 1) + 1))
                        .hotel(hotel)
                        .build();
                room = roomRepository.save(room);

                for (int k = 0; k < 4; ) {
                    UnavailableDate date = new UnavailableDate();
                    date.setDate(LocalDate.now().plusDays(++k + j));
                    date.setRoom(room);
                    unavailableDateRepository.save(date);
                }
            }
        }

        for (int i = 0; i < countUser; ) {
            User user = User.builder()
                    .name("User_" + ++i)
                    .password("pass")
                    .email("mail_" + i)
                    .build();

            UserRole role = UserRole.from(i % 2 == 0 ? RoleType.ROLE_USER : RoleType.ROLE_ADMIN, user);

            user.setRoles(Collections.singletonList(role));

            userRepository.saveAndFlush(user);
        }
    }
}
