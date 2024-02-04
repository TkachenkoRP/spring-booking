package ru.tkachenko.springbooking.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.tkachenko.springbooking.dto.UpsertRoomRequest;
import ru.tkachenko.springbooking.model.Room;
import ru.tkachenko.springbooking.service.HotelService;

public abstract class RoomMapperDelegate implements RoomMapper {
    @Autowired
    private HotelService hotelService;

    @Override
    public Room requestToEntity(UpsertRoomRequest request) {
        Room room = Room.builder()
                .name(request.getName())
                .description(request.getDescription())
                .number(request.getNumber())
                .price(request.getPrice())
                .capacity(request.getCapacity())
                .hotel(hotelService.findById(request.getHotelId()))
                .build();
        return room;
    }

    @Override
    public Room requestToEntity(Long id, UpsertRoomRequest request) {
        Room room = requestToEntity(request);
        room.setId(id);
        return room;
    }

    @Override
    public UpsertRoomRequest entityToUpsertRequest(Room room) {
        UpsertRoomRequest request = new UpsertRoomRequest();
        request.setName(room.getName());
        request.setDescription(room.getDescription());
        request.setNumber(room.getNumber());
        request.setPrice(room.getPrice());
        request.setCapacity(room.getCapacity());
        request.setHotelId(room.getHotel().getId());
        return request;
    }
}
