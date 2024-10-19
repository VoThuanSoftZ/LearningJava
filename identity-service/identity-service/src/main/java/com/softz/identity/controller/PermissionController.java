package com.softz.identity.controller;

import com.softz.identity.dto.ApiResponse;
import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.request.NewPermissionRequest;
import com.softz.identity.service.PermissionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permission")
    public ApiResponse<PermissionDto> createPermission(@RequestBody NewPermissionRequest request) {
        var userDto = permissionService.createPermission(request);
        return ApiResponse.<PermissionDto>builder()
                .result(userDto)
                .build();
    }

    @GetMapping("/permissions")
    public ApiResponse<List<PermissionDto>> getPermissions() {
        var permissions = permissionService.getPermissions();
        return ApiResponse.<List<PermissionDto>>builder()
        .result(permissions)
        .build();
    }

    @GetMapping("/permission/{id}")
    public ApiResponse<PermissionDto> getPermissionById(@PathVariable("id") int id) {
        var result = permissionService.getPermissionById(id);
        return ApiResponse.<PermissionDto>builder()
        .result(result)
        .build();
    }

    @GetMapping("/permission/name/{name}")
    public ApiResponse<PermissionDto> getPermissionByName(@PathVariable("name") String name) {
        var result = permissionService.getPermissionByName(name);
        return ApiResponse.<PermissionDto>builder()
        .result(result)
        .build();
    }

    @PutMapping("permission/{id}")
    public ApiResponse<PermissionDto> putPermission(@PathVariable int id, @RequestBody NewPermissionRequest request) {
        var result = permissionService.updatePermission(id, request);
        return ApiResponse.<PermissionDto>builder()
        .result(result)
        .build();
    }

    @DeleteMapping("permission/{id}")
    public ApiResponse<Boolean> deletePermission(@PathVariable int id) {
        var result = permissionService.deletePermission(id);
        return ApiResponse.<Boolean>builder()
        .result(result)
        .build();
    }
}
