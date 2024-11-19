package com.softz.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.softz.identity.dto.UserDto;
import com.softz.identity.dto.request.NewUserRequest;
import com.softz.identity.dto.request.UpdateUserRequest;
import com.softz.identity.entity.User;
import com.softz.identity.entity.projection.UserBasicInfo;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "userId", source = "id")
    UserDto toUserDto(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toUser(NewUserRequest newUserRequest);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    User toUser(UpdateUserRequest updateUserRequest);

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "dob", ignore = true)
    UserDto toUserDto(UserBasicInfo basicInfo);

}
