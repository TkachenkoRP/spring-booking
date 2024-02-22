package ru.tkachenko.springbooking.mapper;

import org.mapstruct.*;
import ru.tkachenko.springbooking.dto.BookingResponse;
import ru.tkachenko.springbooking.dto.UpsertBookingRequest;
import ru.tkachenko.springbooking.model.Booking;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RoomMap.class, RoomMapper.class})
public interface BookingMapper {
    @Mappings({
            @Mapping(target = "room", source = "roomId")
    })
    Booking requestToEntity(UpsertBookingRequest request);

    @Mappings({
            @Mapping(target = "room", source = "request.roomId")
    })
    Booking requestToEntity(Long id, UpsertBookingRequest request);

    BookingResponse entityToResponse(Booking booking);
}
