package com.softz.identity.controller;

import com.softz.identity.dto.ApiResponse;
import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.dto.request.UpdateUserRequest;
import com.softz.identity.service.UserService;
import com.softz.identity.service.coordinator.UserCoordinatorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserCoordinatorService userCoordinatorService;

    @PostMapping("/users")
    public ApiResponse<UserDto> createUser(@RequestBody @Valid NewUserRequest request) {
        var userDto = userService.createUser(request);
        return ApiResponse.<UserDto>builder()
                .result(userDto)
                .build();
    }

    @PostMapping("/user-with-roles")
    public ApiResponse<UserDto> createUserWithRoles(@RequestBody @Valid NewUserRequest request) {
        var userDto = userCoordinatorService.createUserWithRoles(request);
        return ApiResponse.<UserDto>builder()
                .result(userDto)
                .build();
    }

    @GetMapping("/users")
    public ApiResponse<List<UserDto>> getUsers() {
        var users = userService.getUsers();
        return ApiResponse.<List<UserDto>>builder()
        .result(users)
        .build();
    }

    @GetMapping("/user/{id}")
    public ApiResponse<UserDto> getUserById(@PathVariable("id") String userId) {
        var result = userService.getUserById(userId);
        return ApiResponse.<UserDto>builder()
        .result(result)
        .build();
    }

    @GetMapping("/username/{username}")
    public ApiResponse<UserDto> getUserByUsername(@PathVariable("username") String username) {
        var result = userService.getUserByUsername(username);
        return ApiResponse.<UserDto>builder()
        .result(result)
        .build();
    }

    @PutMapping("user/{id}/password")
    public ApiResponse<UserDto> putPassword(@PathVariable String id, @RequestBody String password) {        
        var result = userService.updatePassword(id, password);
        return ApiResponse.<UserDto>builder()
        .result(result)
        .build();
    }
    
    @PutMapping("user/{id}")
    public ApiResponse<UserDto> putUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {        
        var result = userCoordinatorService.updateUserWithRoles(id, request);
        return ApiResponse.<UserDto>builder()
        .result(result)
        .build();
    }
}
