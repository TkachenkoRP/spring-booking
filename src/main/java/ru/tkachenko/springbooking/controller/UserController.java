package ru.tkachenko.springbooking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.*;
import ru.tkachenko.springbooking.mapper.UserMapper;
import ru.tkachenko.springbooking.model.User;
import ru.tkachenko.springbooking.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Users API")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Value("${app.kafka.kafkaUserRegistryEventTopic}")
    private String topicName;

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    @Operation(
            summary = "Create user"
    )
    @ApiResponses({
            @ApiResponse(
                    description = "User created successfully",
                    responseCode = "201",
                    content = {
                            @Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")
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
    @Parameter(
            name = "roleType",
            description = "Type of the user role",
            in = ParameterIn.QUERY
    )
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UpsertUserRequest request,
                                               @RequestParam String roleType) {
        User newUser = userService.save(userMapper.requestToEntity(request), roleType);
        UserRegisteredEvent event = new UserRegisteredEvent(newUser.getId());
        kafkaTemplate.send(topicName, event);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.entityToResponse(newUser));
    }
}
