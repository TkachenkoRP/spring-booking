package ru.tkachenko.springbooking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.tkachenko.springbooking.dto.HotelResponse;
import ru.tkachenko.springbooking.dto.UpsertHotelRequest;
import ru.tkachenko.springbooking.model.Hotel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {
    Hotel requestToEntity(UpsertHotelRequest request);

    Hotel requestToEntity(Long id, UpsertHotelRequest request);

    UpsertHotelRequest entityToUpsertRequest(Hotel hotel);

    HotelResponse entityToResponse(Hotel hotel);
}
