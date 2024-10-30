package com.softz.identity.service;

import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.entity.User;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.mapper.UserMapper;
import com.softz.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    public User save(User user) {
        return userRepository.save(user);
    }    
    
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }
    public List<User> findAllById(Iterable<String> id) {
        return userRepository.findAllById(id);
    }
    public UserDto createUser(NewUserRequest newUserRequest) {
        // Mapping to User entity
        User user = userMapper.toUser(newUserRequest);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED, user.getUsername());
        }
        return userMapper.toUserDto(user);
    }

    public UserDto getUserById(String userId) {
        return userRepository.findById(userId)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_FOUND, userId));
    }
    
    public UserDto getUserDtoByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, username));
    }
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, username));
    }
    
    public UserDto updatePassword(String id, String passowrd) {
        // Mapping to User entity
        var ouser = userRepository.findById(id);
        var user = ouser.get();
        user.setPassword(passowrd);
        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED, user.getUsername());
        }
        return userMapper.toUserDto(user);
    }


    public List<UserDto> getUsers() {
        return userRepository.findAll()
            .stream()
            .map(userMapper::toUserDto)
            .toList();
    }
}
