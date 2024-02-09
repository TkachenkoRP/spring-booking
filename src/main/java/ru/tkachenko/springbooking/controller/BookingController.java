package ru.tkachenko.springbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.BookingResponse;
import ru.tkachenko.springbooking.dto.RoomBookedEvent;
import ru.tkachenko.springbooking.dto.UpsertBookingRequest;
import ru.tkachenko.springbooking.mapper.BookingMapper;
import ru.tkachenko.springbooking.model.Booking;
import ru.tkachenko.springbooking.security.AppUserPrincipal;
import ru.tkachenko.springbooking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @Value("${app.kafka.kafkaRoomBookedEventTopic}")
    private String topicName;

    private final KafkaTemplate<String, RoomBookedEvent> kafkaTemplate;

    @GetMapping
    public ResponseEntity<List<BookingResponse>> findAll() {
        return ResponseEntity.ok(bookingService.findAll()
                .stream().map(bookingMapper::entityToResponse)
                .toList());
    }

    @PostMapping
    public ResponseEntity<BookingResponse> create(@RequestBody @Valid UpsertBookingRequest request, @AuthenticationPrincipal AppUserPrincipal userPrincipal) {
        Booking newBooking = bookingService.save(userPrincipal.getUser(), bookingMapper.requestToEntity(request));
        RoomBookedEvent event = new RoomBookedEvent(newBooking.getUser().getId(), newBooking.getArrivalDate().toString(), newBooking.getDepartureDate().toString());
        kafkaTemplate.send(topicName, event);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingMapper.entityToResponse(newBooking));
    }
}
