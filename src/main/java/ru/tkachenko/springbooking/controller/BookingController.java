package ru.tkachenko.springbooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.*;
import ru.tkachenko.springbooking.mapper.BookingMapper;
import ru.tkachenko.springbooking.model.Booking;
import ru.tkachenko.springbooking.security.AppUserPrincipal;
import ru.tkachenko.springbooking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Bookings API")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @Value("${app.kafka.kafkaRoomBookedEventTopic}")
    private String topicName;

    private final KafkaTemplate<String, RoomBookedEvent> kafkaTemplate;

    @Operation(
            summary = "Get bookings",
            description = "Retrieve all bookings"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Successful response",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = BookingResponse.class), mediaType = "application/json")
                    }
            )}
    )
    @GetMapping
    public ResponseEntity<List<BookingResponse>> findAll() {
        return ResponseEntity.ok(bookingService.findAll()
                .stream().map(bookingMapper::entityToResponse)
                .toList());
    }

    @Operation(
            summary = "Create booking"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Booking created successfully",
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = BookingResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Bad request",
                    responseCode = "400",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )}
    )
    @PostMapping
    public ResponseEntity<BookingResponse> create(@RequestBody @Valid UpsertBookingRequest request, @AuthenticationPrincipal AppUserPrincipal userPrincipal) {
        Booking newBooking = bookingService.save(userPrincipal.getUser(), bookingMapper.requestToEntity(request));
        RoomBookedEvent event = new RoomBookedEvent(newBooking.getUser().getId(), newBooking.getArrivalDate().toString(), newBooking.getDepartureDate().toString());
        kafkaTemplate.send(topicName, event);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingMapper.entityToResponse(newBooking));
    }
}
