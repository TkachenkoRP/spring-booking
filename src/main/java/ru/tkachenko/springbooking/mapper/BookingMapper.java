package ru.tkachenko.springbooking.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.tkachenko.springbooking.dto.BookingResponse;
import ru.tkachenko.springbooking.dto.UpsertBookingRequest;
import ru.tkachenko.springbooking.model.Booking;

@DecoratedWith(BookingMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RoomMapper.class, UserMapper.class})
public interface BookingMapper {
    Booking requestToEntity(UpsertBookingRequest request);

    Booking requestToEntity(Long id, UpsertBookingRequest request);

    BookingResponse entityToResponse(Booking booking);
}
