package com.softz.identity.service;

import com.softz.dto.UserDto;
import com.softz.dto.request.NewUserRequest;
import com.softz.identity.entity.User;
import com.softz.identity.mapper.UserMapper;
import com.softz.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    // public UserService(UserRepository userRepository, UserMapper userMapper) {
    // this.userRepository = userRepository;
    // this.userMapper = userMapper;
    // }

    // public UserService(UserRepository userRepository) {
    // System.out.println("UserRepository:" + userRepository);
    // this.userRepository = userRepository;
    // }

    public UserDto createUser(NewUserRequest newUserRequest) {
        // Mapping to User entity
        User user = userMapper.toUser(newUserRequest);
        user = userRepository.save(user);
        UserDto userDto = userMapper.toUserDto(user);

        return userDto;
    }

    public UserDto getUserById(String userId) {
        return userRepository.findById(userId)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public UserDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    


    public List<UserDto> getUsers() {
        // List<User> userList = userRepository.findAll();
        // List<UserDto> userDtos = new ArrayList<>();

        // for (User user : userList) {
        // userDtos.add(userMapper.toUserDto(user));
        // }
        // // userRepository.findAll();
        // return userDtos;

        return userRepository.findAll()
            .stream()
            .map(userMapper::toUserDto)
            .toList();

    }
}
