package com.softz.identity.controller;

import com.softz.dto.UserDto;
import com.softz.dto.request.NewUserRequest;
import com.softz.identity.service.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public UserDto createUser(@RequestBody NewUserRequest user) {
        return userService.createUser(user);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/user/{id}")
    public UserDto getUserById(@PathVariable("id") String userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/username/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username) {
        return userService.getUserByUsername(username);
    }


}
