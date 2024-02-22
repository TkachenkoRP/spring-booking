package ru.tkachenko.springbooking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tkachenko.springbooking.model.Room;
import ru.tkachenko.springbooking.service.RoomService;

@Component
@RequiredArgsConstructor
public class RoomMap {
    private final RoomService roomService;

    public Room fromId(Long id) {
        return id != null ? roomService.findById(id) : null;
    }
}
