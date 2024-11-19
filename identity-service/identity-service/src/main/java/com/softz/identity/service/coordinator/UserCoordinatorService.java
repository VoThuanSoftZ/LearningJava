package com.softz.identity.service.coordinator;

import com.softz.identity.dto.RoleDto;
import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewRoleRequest;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.dto.request.UpdateUserRequest;
import com.softz.identity.entity.Role;
import com.softz.identity.entity.User;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.mapper.UserMapper;
import com.softz.identity.service.RoleService;
import com.softz.identity.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserCoordinatorService {
    UserService userService;
    RoleService roleService;
    UserMapper userMapper;

    public UserDto createUser(NewUserRequest newUserRequest) {
        List<Integer> idList = newUserRequest.getRoleIds();

        // Validate duplicated item(s)
        if (idList.stream().distinct().count() != idList.size()) {
            throw new AppException(ErrorCode.INVALID_ROLE);
        }

        // Validate invalid role(s)
        List<Role> roles = roleService.getRoles(idList);
        if (idList.size() != roles.size()) {
            throw new AppException(ErrorCode.INVALID_ROLE);
        }

        // Mapping to User entity
        User user = userMapper.toUser(newUserRequest);

        // Trying ti persist User
        try {
            user.setRoles(Set.copyOf(roles));

            BCryptPasswordEncoder bCryptPasswordEncoder =
                    new BCryptPasswordEncoder(14);

            long started = System.currentTimeMillis();
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            long ended = System.currentTimeMillis();
            log.info("Times to encode: {}", ended - started);

            user = userService.save(user);
        } catch (DataIntegrityViolationException exception) {
            ConstraintViolationException cause =
                    (ConstraintViolationException) exception.getCause();
            throw new AppException(ErrorCode.INVALID_FIELD, cause.getConstraintName(), cause.getMessage());
        }

        // Mapping to UserDto
        return userMapper.toUserDto(user);
    }

    public UserDto updateUserWithRoles(String id, UpdateUserRequest request) {
        var user = userService.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(request.getPassword() != null && !request.getPassword().isEmpty())
            user.setPassword(request.getPassword());
        if(request.getDob() != null)
            user.setDob(request.getDob());
        if(request.getEmail() != null && !request.getEmail().isEmpty())
            user.setEmail(request.getEmail());

        if(request.getRoleIds() != null && request.getRoleIds().size() > 0){
            var roles = roleService.findAllById(request.getRoleIds()).stream().collect(Collectors.toSet());
            user.setRoles(roles);
        }
        user = userService.save(user);        
        return userMapper.toUserDto(user);
    }
}
