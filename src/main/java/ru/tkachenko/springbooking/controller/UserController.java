package ru.tkachenko.springbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.UpsertUserRequest;
import ru.tkachenko.springbooking.dto.UserRegisteredEvent;
import ru.tkachenko.springbooking.dto.UserResponse;
import ru.tkachenko.springbooking.mapper.UserMapper;
import ru.tkachenko.springbooking.model.User;
import ru.tkachenko.springbooking.service.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Value("${app.kafka.kafkaUserRegistryEventTopic}")
    private String topicName;

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

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
