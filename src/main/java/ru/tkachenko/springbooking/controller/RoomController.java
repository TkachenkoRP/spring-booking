package ru.tkachenko.springbooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.*;
import ru.tkachenko.springbooking.mapper.RoomMapper;
import ru.tkachenko.springbooking.model.Room;
import ru.tkachenko.springbooking.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "Rooms API")
public class RoomController {
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @Operation(
            summary = "Get rooms",
            description = "Get all rooms"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Successful response",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = RoomResponse.class), mediaType = "application/json")
                    }
            )}
    )
    @Parameter(
            name = "filter",
            description = "Filter options"
    )
    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAll(RoomFilter filter) {
        return ResponseEntity.ok(roomService.findAll(filter).stream().map(roomMapper::entityToResponse).toList());
    }

    @Operation(
            summary = "Get room by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Successful response",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = RoomResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Room not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )}
    )
    @Parameter(
            name = "id",
            description = "ID of the room to retrieve"
    )
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(roomMapper.entityToResponse(roomService.findById(id)));
    }

    @Operation(
            summary = "Create room"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Room created successfully",
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = RoomResponse.class), mediaType = "application/json")
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
    public ResponseEntity<RoomResponse> create(@RequestBody @Valid UpsertRoomRequest request) {
        Room newRoom = roomService.save(roomMapper.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roomMapper.entityToResponse(newRoom));
    }

    @Operation(
            summary = "Edit room"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Room updated successfully",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = RoomResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Bad request",
                    responseCode = "400",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Room not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )}
    )
    @Parameter(
            name = "id",
            description = "ID of the room to update"
    )
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> update(@PathVariable Long id,
                                               @RequestBody @Valid UpsertRoomRequest request) {
        Room updatedRoom = roomService.update(roomMapper.requestToEntity(id, request));
        return ResponseEntity.ok(roomMapper.entityToResponse(updatedRoom));
    }

    @Operation(
            summary = "Delete room by ID"
    )
    @Parameter(
            name = "id",
            description = "ID of the room to delete"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roomService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
