package com.softz.identity.controller;

import com.softz.identity.dto.ApiResponse;
import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.request.NewPermissionRequest;
import com.softz.identity.service.PermissionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permission")
    public ApiResponse<PermissionDto> createUser(@RequestBody NewPermissionRequest request) {
        var userDto = permissionService.createPermission(request);
        return ApiResponse.<PermissionDto>builder()
                .result(userDto)
                .build();
    }

    @GetMapping("/permissions")
    public ApiResponse<List<PermissionDto>> getUsers() {
        var permissions = permissionService.getPermissions();
        return ApiResponse.<List<PermissionDto>>builder()
        .result(permissions)
        .build();
    }

    @GetMapping("/permission/{id}")
    public PermissionDto getUserById(@PathVariable("id") int id) {
        return permissionService.getPermissionById(id);
    }

    @GetMapping("/permission/name/{name}")
    public PermissionDto getUserByUsername(@PathVariable("name") String name) {
        return permissionService.getPermissionByName(name);
    }


}
