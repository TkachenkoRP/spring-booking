package ru.tkachenko.springbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.BookingResponse;
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

    @GetMapping
    private ResponseEntity<List<BookingResponse>> findAll() {
        return ResponseEntity.ok(bookingService.findAll()
                .stream().map(bookingMapper::entityToResponse)
                .toList());
    }

    @PostMapping
    private ResponseEntity<BookingResponse> create(@RequestBody @Valid UpsertBookingRequest request, @AuthenticationPrincipal AppUserPrincipal userPrincipal) {
        Booking newBooking = bookingService.save(userPrincipal.getUser(), bookingMapper.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingMapper.entityToResponse(newBooking));
    }
}
