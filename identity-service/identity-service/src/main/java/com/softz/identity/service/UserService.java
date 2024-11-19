package com.softz.identity.service;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.softz.identity.dto.PageData;
import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.dto.request.UpdateUserRequest;
import com.softz.identity.entity.Role;
import com.softz.identity.entity.User;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.mapper.UserMapper;
import com.softz.identity.repository.RoleRepository;
import com.softz.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;

    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }
    public User save(User user){
        return userRepository.save(user);
    }

    // spotless:off
    public UserDto createUser(NewUserRequest newUserRequest) {

        Optional<User> existingUser = userRepository.findByEmail(newUserRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED, newUserRequest.getEmail());
        }

        Set<Integer> uniqueRoleIds = new HashSet<>(newUserRequest.getRoleIds());
        if (uniqueRoleIds.size() != newUserRequest.getRoleIds().size()) {
            throw new AppException(ErrorCode.DUPLICATE_IDS, "");
        }

        List<Integer> invalidRoleIds = uniqueRoleIds.stream()
                .filter(roleId -> !roleRepository.existsById(roleId))
                .toList();

        if (!invalidRoleIds.isEmpty()) {
            String invalidIds = invalidRoleIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
            throw new AppException(ErrorCode.INVALID_IDS, String.format(invalidIds));
        }
        User user = userMapper.toUser(newUserRequest);

        Set<Role> roles = mapRoleIdsToRoles(newUserRequest.getRoleIds());

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED, user.getUsername());
        }

        return userMapper.toUserDto(user);
    }

    // spotless:on
    private Set<Role> mapRoleIdsToRoles(List<Integer> roleIds) {
        return roleIds.stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public UserDto getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepository
                .findUserBasicInfoByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserDto(user);
    }

    public UserDto getUserById(String userId) {
        return userRepository
                .findById(userId)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_FOUND, userId));
    }

    public User getUserByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_FOUND, username));
    }

    public UserDto getUserDtoByUsername(String username) {
        return userRepository
                .findByUsername(username)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_FOUND, username));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public PageData<UserDto> getUsers(int page, int size, String keyword) {
        Specification<User> query = (root, query1, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(keyword)) {
                String adjustedKeyword = keyword.equals("%") ? "\\%" : keyword.replace("%", "\\%");
                var criterial = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("username"), "%" + adjustedKeyword + "%", '\\'),
                        criteriaBuilder.like(root.get("email"), "%" + adjustedKeyword + "%", '\\'));
                predicates.add(criterial);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[] {}));
        };

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("username").ascending());
        var pageData = userRepository.findAll(query, pageable);
        return PageData.<UserDto>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.stream().map(userMapper::toUserDto).toList())
                .build();
    }

    public UserDto updateUserWithRoles(String id, UpdateUserRequest request) {

        var user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (request.getPassword() != null && !request.getPassword().isEmpty())
            user.setPassword(request.getPassword());
        if (request.getDob() != null)
            user.setDob(request.getDob());
        if (request.getEmail() != null && !request.getEmail().isEmpty())
            user.setEmail(request.getEmail());

        if (request.getRoleIds() != null && request.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.getRoleIds()));
            user.setRoles(roles);
            userRepository.save(user);
        }
        user = userRepository.save(user);
        return userMapper.toUserDto(user);
    }

    public int plus(int a, int b) {
        return a + b;
    }

    public void deleteUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_FOUND, id));

        try {
            userRepository.delete(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.DELETE_USER_FAILED, id);
        }
    }
}
