package com.softz.identity.controller;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import com.softz.identity.dto.ApiResponse;
import com.softz.identity.dto.PageData;
import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.request.NewPermissionRequest;
import com.softz.identity.service.PermissionService;
@Slf4j
@RestController
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    public ApiResponse<PermissionDto> createPermission(@RequestBody @Valid NewPermissionRequest newPermissionRequest) {
        var permissionDto = permissionService.createPermission(newPermissionRequest);
        return ApiResponse.<PermissionDto>builder().result(permissionDto).build();
    }

    @GetMapping("/permissions")
    public ApiResponse<PageData<PermissionDto>> getPermission(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        PageData<PermissionDto> permission = permissionService.getPermissions(page, size);
        return ApiResponse.<PageData<PermissionDto>>builder().result(permission).build();
    }


    @PutMapping("/permission/{id}")
    public ApiResponse<PermissionDto> updatePermission(
            @PathVariable("id") Integer id, @RequestBody @Valid NewPermissionRequest updatePermissionRequest) {
        PermissionDto updatedPermission = permissionService.updatePermission(id, updatePermissionRequest);
        return ApiResponse.<PermissionDto>builder().result(updatedPermission).build();
    }

    @DeleteMapping("/permission/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable("id") Integer id) {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder()
                .message("Permission deleted successfully")
                .build();
    }
}
