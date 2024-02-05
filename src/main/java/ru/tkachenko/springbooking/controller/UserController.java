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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        return ResponseEntity.ok(userService.findAll()
                .stream().map(userMapper::entityToResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userMapper.entityToResponse(userService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UpsertUserRequest request,
                                               @RequestParam String roleType) {
        User newUser = userService.save(userMapper.requestToEntity(request), roleType);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.entityToResponse(newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @RequestBody @Valid UpsertUserRequest request) {
        User updatedUser = userService.update(userMapper.requestToEntity(id, request));
        return ResponseEntity.ok(userMapper.entityToResponse(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
