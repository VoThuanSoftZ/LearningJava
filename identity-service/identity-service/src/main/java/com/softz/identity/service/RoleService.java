package com.softz.identity.service;

import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.RoleDto;
import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewRoleRequest;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.entity.Permission;
import com.softz.identity.entity.User;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.mapper.PermissionMapper;
import com.softz.identity.mapper.RoleMapper;
import com.softz.identity.mapper.UserMapper;
import com.softz.identity.repository.PermissionRepository;
import com.softz.identity.repository.RoleRepository;
import com.softz.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;

    public RoleDto createRole(NewRoleRequest request) {
        // Mapping to Permission entity
        var role = roleMapper.toRole(request);
        try {
            role = roleRepository.save(role);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        return roleMapper.toRoleDto(role);
    }

    public RoleDto getRoleById(int id) {
        return roleRepository.findById(id)
                .map(roleMapper::toRoleDto)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
    }
    
    public RoleDto getRoleByName(String name) {
        return roleRepository.findByName(name)
                .map(roleMapper::toRoleDto)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
    }
    
    public RoleDto updateRole(int id, NewRoleRequest request) {
        // Mapping to Permission entity
        var role = roleRepository.findById(id).get();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        try {
            role = roleRepository.save(role);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        return roleMapper.toRoleDto(role);
    }

    public Boolean deleteRole(int id) {
        // Mapping to Permission entity
        var role = roleRepository.findById(id).get();
        try {
            roleRepository.delete(role);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        return true;
    }

    public List<RoleDto> getRoles() {
        return roleRepository.findAll()
            .stream()
            .map(roleMapper::toRoleDto)
            .toList();
    }
}
