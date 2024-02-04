package ru.tkachenko.springbooking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.HotelListResponse;
import ru.tkachenko.springbooking.dto.HotelResponse;
import ru.tkachenko.springbooking.dto.UpsertHotelRequest;
import ru.tkachenko.springbooking.mapper.HotelMapper;
import ru.tkachenko.springbooking.model.Hotel;
import ru.tkachenko.springbooking.service.HotelService;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @GetMapping
    public ResponseEntity<HotelListResponse> findAll(@RequestParam(defaultValue = "20") int pageSize,
                                                     @RequestParam(defaultValue = "0") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));

        List<HotelResponse> hotelResponses = hotelService.findAll(pageable)
                .stream().map(hotelMapper::entityToResponse)
                .toList();

        return ResponseEntity.ok(new HotelListResponse(hotelResponses, pageable.getPageSize(), pageable.getPageNumber()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelMapper.entityToResponse(hotelService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<HotelResponse> create(@RequestBody UpsertHotelRequest request) {
        Hotel newHotel = hotelService.save(hotelMapper.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelMapper.entityToResponse(newHotel));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> update(@PathVariable Long id,
                                                @RequestBody UpsertHotelRequest request) {
        Hotel updatedHotel = hotelService.update(hotelMapper.requestToEntity(id, request));
        return ResponseEntity.ok(hotelMapper.entityToResponse(updatedHotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
