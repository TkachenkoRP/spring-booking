package ru.tkachenko.springbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.RoomFilter;
import ru.tkachenko.springbooking.dto.RoomResponse;
import ru.tkachenko.springbooking.dto.UpsertRoomRequest;
import ru.tkachenko.springbooking.mapper.RoomMapper;
import ru.tkachenko.springbooking.model.Room;
import ru.tkachenko.springbooking.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAll(RoomFilter filter) {
        return ResponseEntity.ok(roomService.findAll(filter).stream().map(roomMapper::entityToResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomMapper.entityToResponse(roomService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<RoomResponse> create(@RequestBody @Valid UpsertRoomRequest request) {
        Room newRoom = roomService.save(roomMapper.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomMapper.entityToResponse(newRoom));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id,
                                               @RequestBody @Valid UpsertRoomRequest request) {
        Room updatedRoom = roomService.update(roomMapper.requestToEntity(id, request));
        return ResponseEntity.ok(roomMapper.entityToResponse(updatedRoom));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
