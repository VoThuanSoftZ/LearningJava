package com.softz.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.softz.identity.dto.RoleDto;
import com.softz.identity.dto.request.NewRoleRequest;
import com.softz.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "id", source = "id")
    RoleDto toRoleDto(Role user);
    Role toRole(NewRoleRequest roleDto);
}
