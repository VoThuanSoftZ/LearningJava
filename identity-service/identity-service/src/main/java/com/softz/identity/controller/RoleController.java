package com.softz.identity.controller;

import com.softz.identity.dto.ApiResponse;
import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.RoleDto;
import com.softz.identity.dto.request.NewPermissionRequest;
import com.softz.identity.dto.request.NewRoleRequest;
import com.softz.identity.service.RoleService;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/role")
    public ApiResponse<RoleDto> createUser(@RequestBody NewRoleRequest request) {
        var userDto = roleService.createRole(request);
        return ApiResponse.<RoleDto>builder()
                .result(userDto)
                .build();
    }

    @GetMapping("/roles")
    public ApiResponse<List<RoleDto>> getUsers() {
        var roles = roleService.getRoles();
        return ApiResponse.<List<RoleDto>>builder()
        .result(roles)
        .build();
    }

    @GetMapping("/role/{id}")
    public RoleDto getUserById(@PathVariable("id") int id) {
        return roleService.getRoleById(id);
    }

    @GetMapping("/role/name/{name}")
    public RoleDto getUserByUsername(@PathVariable("name") String name) {
        return roleService.getRoleByName(name);
    }

    @PutMapping("role/{id}")
    public RoleDto putPermission(@PathVariable int id, @RequestBody NewRoleRequest request) {
        return roleService.updateRole(id, request);
    }

    @DeleteMapping("permission/{id}")
    public ApiResponse<Boolean> deletePermission(@PathVariable int id) {
        var result = roleService.deleteRole(id);
        return ApiResponse.<Boolean>builder()
        .result(result)
        .build();
    }

}
