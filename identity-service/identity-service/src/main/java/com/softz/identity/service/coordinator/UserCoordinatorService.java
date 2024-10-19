package com.softz.identity.service.coordinator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.RoleDto;
import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewRoleRequest;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.dto.request.UpdateUserRequest;
import com.softz.identity.entity.Permission;
import com.softz.identity.entity.Role;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.mapper.RoleMapper;
import com.softz.identity.mapper.UserMapper;
import com.softz.identity.repository.PermissionRepository;
import com.softz.identity.repository.RoleRepository;
import com.softz.identity.repository.UserRepository;
import com.softz.identity.service.PermissionService;
import com.softz.identity.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCoordinatorService {
    private final RoleService roleService;
    private final PermissionService permissionService;
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    public RoleDto createRoleWithPermissions(NewRoleRequest request) {
        List<Integer> idList = request.getPermissionIds();
        List<Permission> permissions = permissionService.getPermissions(idList);

        // Validate
        if (!CollectionUtils.isEmpty(request.getPermissionIds())
                && idList.size() != permissions.size()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        Role role = roleMapper.toRole(request);
        role.setPermissions(Set.copyOf(permissions));

        return roleMapper.toRoleDto(roleRepository.save(role));
    }
    
    public UserDto createUserWithRoles(NewUserRequest request) {
        List<Integer> idList = request.getRoleIds();
        List<Role> roles = roleService.getRoles(idList);

        // Validate
        if (!CollectionUtils.isEmpty(request.getRoleIds())
                && idList.size() != roles.size()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        var user = userMapper.toUser(request);
        user.setRoles(Set.copyOf(roles));

        return userMapper.toUserDto(userRepository.save(user));
    }

    public RoleDto updateRoleWithPermissions(Integer id, NewRoleRequest request) {
        var role = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(request.getName() != null && !request.getName().isEmpty())
            role.setName(request.getName());
        if(request.getDescription() != null && !request.getDescription().isEmpty())
            role.setDescription(request.getDescription());

        if(request.getPermissionIds() != null && request.getPermissionIds().size() > 0){
            var permissions = permissionRepository.findAllById(request.getPermissionIds()).stream().collect(Collectors.toSet());
            role.setPermissions(permissions);
        }
        role = roleRepository.save(role);        
        return roleMapper.toRoleDto(role);
    }

    public UserDto updateUserWithRoles(String id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(request.getPassword() != null && !request.getPassword().isEmpty())
            user.setPassword(request.getPassword());
        if(request.getDob() != null)
            user.setDob(request.getDob());
        if(request.getEmail() != null && !request.getEmail().isEmpty())
            user.setEmail(request.getEmail());

        if(request.getRoleIds() != null && request.getRoleIds().size() > 0){
            var roles = roleRepository.findAllById(request.getRoleIds()).stream().collect(Collectors.toSet());
            user.setRoles(roles);
        }
        user = userRepository.save(user);        
        return userMapper.toUserDto(user);
    }

}
