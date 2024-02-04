package ru.tkachenko.springbooking.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.tkachenko.springbooking.dto.RoomResponse;
import ru.tkachenko.springbooking.dto.UpsertRoomRequest;
import ru.tkachenko.springbooking.model.Room;

@DecoratedWith(RoomMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoomMapper {
    Room requestToEntity(UpsertRoomRequest request);

    Room requestToEntity(Long id, UpsertRoomRequest request);

    UpsertRoomRequest entityToUpsertRequest(Room room);

    RoomResponse entityToResponse(Room room);
}
