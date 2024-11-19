package com.softz.identity.controller;

import org.springframework.web.bind.annotation.*;

import com.softz.identity.dto.ApiResponse;
import com.softz.identity.dto.RoleDto;
import com.softz.identity.dto.request.NewRoleRequest;
import com.softz.identity.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/role")
    public ApiResponse<RoleDto> createRole(@RequestBody NewRoleRequest newRoleRequest) {
        var roleDto = roleService.createRole(newRoleRequest);
        return ApiResponse.<RoleDto>builder().result(roleDto).build();
    }



}
