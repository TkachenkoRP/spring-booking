package ru.tkachenko.springbooking.service.impl;

import org.springframework.stereotype.Service;
import ru.tkachenko.springbooking.dto.RoomBookedEvent;
import ru.tkachenko.springbooking.dto.UserRegisteredEvent;
import ru.tkachenko.springbooking.service.KafkaService;

import java.util.ArrayList;
import java.util.List;

@Service
public class KafkaServiceImpl<T> implements KafkaService<T> {

    private final List<RoomBookedEvent> eventsBooked = new ArrayList<>();
    private final List<UserRegisteredEvent> eventsUser = new ArrayList<>();


    @Override
    public void add(T event) {
        if (event instanceof RoomBookedEvent) {
            eventsBooked.add((RoomBookedEvent) event);
        } else if (event instanceof UserRegisteredEvent) {
            eventsUser.add((UserRegisteredEvent) event);
        }
    }
}
