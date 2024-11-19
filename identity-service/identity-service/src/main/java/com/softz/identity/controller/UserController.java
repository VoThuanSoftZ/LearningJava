package com.softz.identity.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.softz.identity.dto.ApiResponse;
import com.softz.identity.dto.PageData;
import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.dto.request.UpdateUserRequest;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.service.UserService;
import com.softz.identity.utils.JwtService;

@RestController
public class UserController {

    private final JwtService jwtService;
    private final UserService userService;

    public UserController(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @GetMapping("/user/me")
    public ApiResponse<UserDto> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        UserDto userDto = userService.getMyProfile();
        return ApiResponse.<UserDto>builder().result(userDto).build();
    }

    @PostMapping("/users/registration")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        if (newUserRequest.getRoleIds() == null || newUserRequest.getRoleIds().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_ROLE);
        }

        var userDto = userService.createUser(newUserRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserDto>builder().result(userDto).build());
    }

    @GetMapping("/users")
    public ApiResponse<PageData<UserDto>> getUsers(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", defaultValue = "") String keyword) {
        PageData<UserDto> users = userService.getUsers(page, size, keyword);
        return ApiResponse.<PageData<UserDto>>builder().result(users).build();
    }

    @GetMapping("/user/{id}")
    public ApiResponse<UserDto> getUserById(@PathVariable("id") String userId) {
        UserDto userDto = userService.getUserById(userId);
        return ApiResponse.<UserDto>builder().result(userDto).build();
    }

    @GetMapping("/username/{username}")
    public ApiResponse<UserDto> getUserByUsername(@PathVariable("username") String username) {
        UserDto user = userService.getUserDtoByUsername(username);
        return ApiResponse.<UserDto>builder().result(user).build();
    }

    @PutMapping("user/{id}")
    public ApiResponse<UserDto> putUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        UserDto userDto = userService.updateUserWithRoles(id, request);
        return ApiResponse.<UserDto>builder().result(userDto).build();
    }

    @DeleteMapping("/user/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        userService.deleteUserById(id);
        return ApiResponse.<Void>builder().message("User deleted successfully").build();
    }
}
