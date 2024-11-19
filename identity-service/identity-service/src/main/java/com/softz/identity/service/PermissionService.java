package com.softz.identity.service;

import com.softz.identity.dto.PageData;
import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.request.NewPermissionRequest;
import com.softz.identity.entity.Permission;
import com.softz.identity.entity.Role;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.mapper.PermissionMapper;
import com.softz.identity.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    public List<Permission> findAllById(List<Integer> ids) {
        return permissionRepository.findAllById(ids);
    }

    public PermissionDto createPermission(NewPermissionRequest request) {
        // Mapping to Permission entity
        var permission = permissionMapper.toPermission(request);
        try {
            permission = permissionRepository.save(permission);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.INVALID_PERMISSIONS);
        }
        return permissionMapper.toPermissionDto(permission);
    }

    public PermissionDto getPermissionById(int id) {
        return permissionRepository.findById(id)
                .map(permissionMapper::toPermissionDto)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PERMISSIONS));
    }
    
    public PermissionDto getPermissionByName(String name) {
        return permissionRepository.findByName(name)
                .map(permissionMapper::toPermissionDto)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PERMISSIONS));
    }
    

    public PermissionDto updatePermission(int id, NewPermissionRequest request) {
        // Mapping to Permission entity
        var permission = permissionRepository.findById(id).get();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        try {
            permission = permissionRepository.save(permission);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.INVALID_PERMISSIONS);
        }
        return permissionMapper.toPermissionDto(permission);
    }

    public Boolean deletePermission(int id) {
        var permission = permissionRepository.findById(id).get();
        try {
            permissionRepository.delete(permission);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.INVALID_PERMISSIONS);
        }
        return true;
    }

    public List<PermissionDto> getPermissions() {
        return permissionRepository.findAll()
            .stream()
            .map(permissionMapper::toPermissionDto)
            .toList();
    }

    public PageData<PermissionDto> getPermissions(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("name").ascending());
        var pageData = permissionRepository.findAll(pageable);
        return PageData.<PermissionDto>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.stream().map(permissionMapper::toPermissionDto).toList())
                .build();
    }


    public List<Permission> getPermissions(List<Integer> permissions) {
        return permissionRepository.findByIdIn(permissions);
    }
}
