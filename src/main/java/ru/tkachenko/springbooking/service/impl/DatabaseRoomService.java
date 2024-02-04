package ru.tkachenko.springbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tkachenko.springbooking.exception.EntityNotFoundException;
import ru.tkachenko.springbooking.model.Room;
import ru.tkachenko.springbooking.repository.RoomRepository;
import ru.tkachenko.springbooking.service.RoomService;
import ru.tkachenko.springbooking.utils.BeanUtils;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseRoomService implements RoomService {
    private final RoomRepository repository;

    @Override
    public List<Room> findAll() {
        return repository.findAll();
    }

    @Override
    public Room findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
                        "Комната с ID {0} не найдена!", id
                )));
    }

    @Override
    public Room save(Room room) {
        return repository.save(room);
    }

    @Override
    public Room update(Room room) {
        Room existedRoom = findById(room.getId());
        BeanUtils.copyNonNullProperties(room, existedRoom);
        return repository.save(existedRoom);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
