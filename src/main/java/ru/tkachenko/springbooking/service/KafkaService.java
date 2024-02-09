package ru.tkachenko.springbooking.service;

import ru.tkachenko.springbooking.dto.Event;

public interface KafkaService<T extends Event> {
    void add(T event);
}
