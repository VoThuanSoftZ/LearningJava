package com.softz.identity.controller;

import com.softz.identity.dto.ApiResponse;
import com.softz.identity.dto.RoleDto;
import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewRoleRequest;
import com.softz.identity.dto.request.UpdateUserRequest;
import com.softz.identity.service.RoleService;
import com.softz.identity.service.coordinator.UserCoordinatorService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final UserCoordinatorService userCoordinatorService;

    @PostMapping("/role")
    public ApiResponse<RoleDto> createRole(@RequestBody NewRoleRequest request) {
        var userDto = roleService.createRole(request);
        return ApiResponse.<RoleDto>builder()
                .result(userDto)
                .build();
    }

    @PostMapping("/role-with-permissions")
    public ApiResponse<RoleDto> createRoleWithPermission(@RequestBody NewRoleRequest request) {
        var userDto = userCoordinatorService.createRoleWithPermissions(request);
        return ApiResponse.<RoleDto>builder()
                .result(userDto)
                .build();
    }
    
    @GetMapping("/roles")
    public ApiResponse<List<RoleDto>> getRoles() {
        var roles = roleService.getRoles();
        return ApiResponse.<List<RoleDto>>builder()
        .result(roles)
        .build();
    }

    @GetMapping("/role/{id}")
    public ApiResponse<RoleDto> getRoleById(@PathVariable("id") int id) {
        var result = roleService.getRoleById(id);
        return ApiResponse.<RoleDto>builder()
        .result(result)
        .build();
    }

    @GetMapping("/role/name/{name}")
    public ApiResponse<RoleDto> getRoleByName(@PathVariable("name") String name) {
        var result = roleService.getRoleByName(name);
        return ApiResponse.<RoleDto>builder()
        .result(result)
        .build();
    }

    @DeleteMapping("role/{id}")
    public ApiResponse<Boolean> deleteRole(@PathVariable int id) {
        var result = roleService.deleteRole(id);
        return ApiResponse.<Boolean>builder()
        .result(result)
        .build();
    }

    @PutMapping("role/{id}")
    public ApiResponse<RoleDto> putRole(@PathVariable int id, @RequestBody NewRoleRequest request) {        
        var result = userCoordinatorService.updateRoleWithPermissions(id, request);
        return ApiResponse.<RoleDto>builder()
        .result(result)
        .build();
    }
}
