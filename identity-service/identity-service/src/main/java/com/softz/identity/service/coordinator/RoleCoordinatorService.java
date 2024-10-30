package com.softz.identity.service.coordinator;

import com.softz.identity.dto.RoleDto;
import com.softz.identity.dto.request.NewRoleRequest;
import com.softz.identity.entity.Permission;
import com.softz.identity.entity.Role;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.mapper.RoleMapper;
import com.softz.identity.service.PermissionService;
import com.softz.identity.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleCoordinatorService {
    PermissionService permissionService;
    RoleService roleService;
    RoleMapper roleMapper;
    
    public RoleDto createRole(NewRoleRequest request) {
        List<Integer> idList = request.getPermissionIds();
        List<Permission> permissions = permissionService.getPermissions(
                CollectionUtils.isEmpty(idList) ? Collections.emptyList() : idList);

        // Validate
        if (!CollectionUtils.isEmpty(request.getPermissionIds())
                && idList.size() != permissions.size()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        Role role = roleMapper.toRole(request);
        role.setPermissions(Set.copyOf(permissions));

        return roleMapper.toRoleDto(roleService.save(role));
    }

    public RoleDto updateRole(Integer id, NewRoleRequest request) {
        var role = roleService.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(request.getName() != null && !request.getName().isEmpty())
            role.setName(request.getName());
        if(request.getDescription() != null && !request.getDescription().isEmpty())
            role.setDescription(request.getDescription());

        if(request.getPermissionIds() != null && request.getPermissionIds().size() > 0){
            var permissions = permissionService.findAllById(request.getPermissionIds()).stream().collect(Collectors.toSet());
            role.setPermissions(permissions);
        }
        role = roleService.save(role);        
        return roleMapper.toRoleDto(role);
    }

}
