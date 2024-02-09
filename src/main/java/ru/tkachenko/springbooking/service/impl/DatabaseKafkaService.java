package ru.tkachenko.springbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springbooking.dto.Event;
import ru.tkachenko.springbooking.dto.RoomBookedEvent;
import ru.tkachenko.springbooking.dto.UserRegisteredEvent;
import ru.tkachenko.springbooking.repository.RoomBookedEventRepository;
import ru.tkachenko.springbooking.repository.UserRegisteredEventRepository;
import ru.tkachenko.springbooking.service.KafkaService;

@Service
@RequiredArgsConstructor
public class DatabaseKafkaService<T extends Event> implements KafkaService<T> {
    private final RoomBookedEventRepository roomBookedEventRepository;
    private final UserRegisteredEventRepository userRegisteredEventRepository;


    @Override
    public void add(T event) {
        if (event instanceof RoomBookedEvent) {
            roomBookedEventRepository.save((RoomBookedEvent) event);
        } else if (event instanceof UserRegisteredEvent) {
            userRegisteredEventRepository.save((UserRegisteredEvent) event);
        }
    }
}
