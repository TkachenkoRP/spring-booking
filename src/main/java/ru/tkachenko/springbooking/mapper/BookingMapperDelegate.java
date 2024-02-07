package ru.tkachenko.springbooking.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.tkachenko.springbooking.dto.UpsertBookingRequest;
import ru.tkachenko.springbooking.model.Booking;
import ru.tkachenko.springbooking.service.RoomService;

public abstract class BookingMapperDelegate implements BookingMapper {
    @Autowired
    private RoomService roomService;

    @Override
    public Booking requestToEntity(UpsertBookingRequest request) {
        Booking booking = new Booking();
        booking.setArrivalDate(request.getArrivalDate());
        booking.setDepartureDate(request.getDepartureDate());
        booking.setRoom(roomService.findById(request.getRoomId()));
        return booking;
    }

    @Override
    public Booking requestToEntity(Long id, UpsertBookingRequest request) {
        Booking booking = requestToEntity(request);
        booking.setId(id);
        return booking;
    }
}
