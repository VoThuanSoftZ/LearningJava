package com.softz.identity.service;

import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewPermissionRequest;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.entity.Permission;
import com.softz.identity.entity.User;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.mapper.PermissionMapper;
import com.softz.identity.mapper.UserMapper;
import com.softz.identity.repository.PermissionRepository;
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
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionDto createPermission(NewPermissionRequest request) {
        // Mapping to Permission entity
        var permission = permissionMapper.toPermission(request);
        try {
            permission = permissionRepository.save(permission);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        return permissionMapper.toPermissionDto(permission);
    }

    public PermissionDto getPermissionById(int id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toPermissionDto)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
    }
    
    public PermissionDto getPermissionByName(String name) {
        return permissionRepository.findByName(name)
                .map(permissionMapper::toPermissionDto)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
    }
    

    public PermissionDto updatePermission(int id, NewPermissionRequest request) {
        // Mapping to Permission entity
        var permission = permissionRepository.findById(id).get();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        try {
            permission = permissionRepository.save(permission);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.PERMISSION_NOT_FOUND);
        }
        return permissionMapper.toPermissionDto(permission);
    }

    public Boolean deletePermission(int id) {
        var permission = permissionRepository.findById(id).get();
        try {
            permissionRepository.delete(permission);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }
        return true;
    }

    public List<PermissionDto> getPermissions() {
        return permissionRepository.findAll()
            .stream()
            .map(permissionMapper::toPermissionDto)
            .toList();
    }
}
