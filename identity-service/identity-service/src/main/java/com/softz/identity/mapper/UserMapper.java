package com.softz.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", source = "id")
    UserDto toUserDto(User user);

    User toUser(NewUserRequest newUserRequest);
}
