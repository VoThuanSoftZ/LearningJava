package com.softz.identity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private int id;
    private String name;
    private String description;
    private PermissionDto[] permissions;
}
