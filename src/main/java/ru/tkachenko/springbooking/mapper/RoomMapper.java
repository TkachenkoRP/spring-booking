package ru.tkachenko.springbooking.mapper;

import org.mapstruct.*;
import ru.tkachenko.springbooking.dto.RoomResponse;
import ru.tkachenko.springbooking.dto.UpsertRoomRequest;
import ru.tkachenko.springbooking.model.Room;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {HotelMap.class})
public interface RoomMapper {
    @Mappings({
            @Mapping(target = "hotel", source = "hotelId")
    })
    Room requestToEntity(UpsertRoomRequest request);

    @Mappings({
            @Mapping(target = "hotel", source = "request.hotelId")
    })
    Room requestToEntity(Long id, UpsertRoomRequest request);

    @Mappings({
            @Mapping(target = "hotelId", source = "hotel.id")
    })
    UpsertRoomRequest entityToUpsertRequest(Room room);

    @Mappings({
            @Mapping(target = "hotelName", source = "hotel.name"),
            @Mapping(target = "hotelId", source = "hotel.id")
    })
    RoomResponse entityToResponse(Room room);
}
