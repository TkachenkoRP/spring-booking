package ru.tkachenko.springbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.tkachenko.springbooking.controller.specification.RoomSpecification;
import ru.tkachenko.springbooking.dto.RoomFilter;
import ru.tkachenko.springbooking.exception.DateException;
import ru.tkachenko.springbooking.exception.EntityNotFoundException;
import ru.tkachenko.springbooking.model.Room;
import ru.tkachenko.springbooking.repository.RoomRepository;
import ru.tkachenko.springbooking.service.RoomService;
import ru.tkachenko.springbooking.utils.BeanUtils;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseRoomService implements RoomService {
    private final RoomRepository repository;

    @Override
    public List<Room> findAll(RoomFilter filter) {

        if (filter.getArrivalDate() != null && filter.getDepartureDate() != null) {
            LocalDate fromDate = LocalDate.parse(filter.getArrivalDate());
            LocalDate toDate = LocalDate.parse(filter.getDepartureDate());

            if (fromDate.isAfter(toDate)) {
                throw new DateException("Дата заезда не может быть позже даты выезда!");
            }

            if (fromDate.isBefore(LocalDate.now()) || toDate.isBefore(LocalDate.now())) {
                throw new DateException("Нельзя указывать прошедшие даты!");
            }
        }

        return repository.findAll(RoomSpecification.withFilter(filter),
                PageRequest.of(
                        filter.getPageNumber(), filter.getPageSize()
                )).getContent();
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
