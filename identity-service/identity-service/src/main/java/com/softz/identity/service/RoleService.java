package com.softz.identity.service;

import com.softz.identity.dto.RoleDto;
import com.softz.identity.dto.request.NewRoleRequest;
import com.softz.identity.entity.Permission;
import com.softz.identity.entity.Role;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.mapper.RoleMapper;
import com.softz.identity.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    //PermissionService permissionService;

    public RoleDto createRole(NewRoleRequest request) {
        Role role = roleMapper.toRole(request);
        try {
            role = roleRepository.save(role);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED, role.getName());
        }
        return roleMapper.toRoleDto(roleRepository.save(role));
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

    public List<Role> getRoles(List<Integer> roles) {
        return roleRepository.findByIdIn(roles);
    }
}
