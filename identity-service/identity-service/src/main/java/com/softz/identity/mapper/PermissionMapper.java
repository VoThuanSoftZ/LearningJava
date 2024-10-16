package com.softz.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.softz.identity.dto.PermissionDto;
import com.softz.identity.dto.request.NewPermissionRequest;
import com.softz.identity.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    @Mapping(target = "id", source = "id")
    PermissionDto toPermissionDto(Permission user);

    Permission toPermission(NewPermissionRequest permissionDto);
}
