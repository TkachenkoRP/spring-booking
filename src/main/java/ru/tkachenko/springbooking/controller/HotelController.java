package ru.tkachenko.springbooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.*;
import ru.tkachenko.springbooking.mapper.HotelMapper;
import ru.tkachenko.springbooking.model.Hotel;
import ru.tkachenko.springbooking.service.HotelService;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
@Tag(name = "Hotels", description = "Hotels API")
public class HotelController {
    private final HotelService hotelService;
    private final HotelMapper hotelMapper;

    @Operation(
            summary = "Get hotels",
            description = "Retrieve all hotels"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Successful response",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = HotelListResponse.class), mediaType = "application/json")
                    }
            )}
    )
    @Parameters({
            @Parameter(
                    name = "filter",
                    description = "Filter options"
            ),
            @Parameter(
                    name = "pageSize",
                    description = "Number of items per page"
            ),
            @Parameter(
                    name = "pageNumber",
                    description = "Page number"
            )
    })
    @GetMapping
    public ResponseEntity<HotelListResponse> findAll(@ModelAttribute HotelFilter filter,
                                                     @RequestParam(defaultValue = "20") int pageSize,
                                                     @RequestParam(defaultValue = "0") int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("id"));

        List<HotelResponse> hotelResponses = hotelService.findAll(pageable, filter)
                .stream().map(hotelMapper::entityToResponse)
                .toList();

        return ResponseEntity.ok(new HotelListResponse(
                        hotelResponses,
                        hotelService.count(),
                        pageable.getPageSize(),
                        pageable.getPageNumber()
                )
        );
    }

    @Operation(
            summary = "Get hotel by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Successful response",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = HotelResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(
                    description = "Hotel not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )}
    )
    @Parameter(
            name = "id",
            description = "ID of the hotel to retrieve"
    )
    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelMapper.entityToResponse(hotelService.findById(id)));
    }

    @Operation(
            summary = "Create hotel"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Hotel created successfully",
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = HotelResponse.class), mediaType = "application/json")
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
    public ResponseEntity<HotelResponse> create(@RequestBody @Valid UpsertHotelRequest request) {
        Hotel newHotel = hotelService.save(hotelMapper.requestToEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hotelMapper.entityToResponse(newHotel));
    }

    @Operation(
            summary = "Edit hotel"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Hotel updated successfully",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = HotelResponse.class), mediaType = "application/json")
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
                    description = "Hotel not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )}
    )
    @Parameter(
            name = "id",
            description = "ID of the hotel to update"
    )
    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> update(@PathVariable Long id,
                                                @RequestBody @Valid UpsertHotelRequest request) {
        Hotel updatedHotel = hotelService.update(hotelMapper.requestToEntity(id, request));
        return ResponseEntity.ok(hotelMapper.entityToResponse(updatedHotel));
    }

    @Operation(
            summary = "Delete hotel by ID"
    )
    @Parameter(
            name = "id",
            description = "ID of the hotel to delete"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        hotelService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Vote",
            description = "Vote for the hotel"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "Successful vote",
                    responseCode = "200",
                    content = {
                            @Content(schema = @Schema(implementation = HotelResponse.class), mediaType = "application/json")
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
                    description = "Hotel not found",
                    responseCode = "404",
                    content = {
                            @Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    }
            )}
    )
    @Parameters({
            @Parameter(
                    name = "id",
                    description = "ID of the hotel to vote for"
            ),
            @Parameter(
                    name = "newMark",
                    description = "New mark for the hotel"
            )
    })
    @PutMapping("/{id}/vote/{newMark}")
    public ResponseEntity<HotelResponse> vote(@PathVariable Long id, @PathVariable int newMark) {
        Hotel hotel = hotelService.updateRating(id, newMark);
        return ResponseEntity.ok(hotelMapper.entityToResponse(hotel));
    }
}
