package ru.tkachenko.springbooking.service;

public interface KafkaService<T> {
    void add(T event);
}
