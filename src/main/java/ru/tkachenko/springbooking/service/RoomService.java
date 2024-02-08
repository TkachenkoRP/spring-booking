package ru.tkachenko.springbooking.service;

import ru.tkachenko.springbooking.dto.RoomFilter;
import ru.tkachenko.springbooking.model.Room;

import java.util.List;

public interface RoomService {
    List<Room> findAll(RoomFilter filter);

    Room findById(Long id);

    Room save(Room room);

    Room update(Room room);

    void deleteById(Long id);
}
