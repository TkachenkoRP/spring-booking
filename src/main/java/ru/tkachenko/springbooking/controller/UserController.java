package ru.tkachenko.springbooking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tkachenko.springbooking.dto.UpsertUserRequest;
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

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UpsertUserRequest request,
                                               @RequestParam String roleType) {
        User newUser = userService.save(userMapper.requestToEntity(request), roleType);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.entityToResponse(newUser));
    }
}
