package ru.tkachenko.springbooking.mapper;

import org.mapstruct.*;
import ru.tkachenko.springbooking.dto.HotelResponse;
import ru.tkachenko.springbooking.dto.UpsertHotelRequest;
import ru.tkachenko.springbooking.model.Hotel;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HotelMapper {
    Hotel requestToEntity(UpsertHotelRequest request);

    Hotel requestToEntity(Long id, UpsertHotelRequest request);

    UpsertHotelRequest entityToUpsertRequest(Hotel hotel);

    HotelResponse entityToResponse(Hotel hotel);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "rating", ignore = true),
            @Mapping(target = "numberOfRatings", ignore = true),
            @Mapping(target = "createAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "rooms", ignore = true)
    })
    void updateHotel(Hotel sourceHotel, @MappingTarget Hotel targetHotel);
}
