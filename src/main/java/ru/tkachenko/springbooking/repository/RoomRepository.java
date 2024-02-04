package ru.tkachenko.springbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tkachenko.springbooking.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
