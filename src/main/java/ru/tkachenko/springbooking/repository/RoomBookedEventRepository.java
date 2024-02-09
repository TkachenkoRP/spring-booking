package ru.tkachenko.springbooking.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.tkachenko.springbooking.dto.RoomBookedEvent;

public interface RoomBookedEventRepository extends MongoRepository<RoomBookedEvent, String> {
}
