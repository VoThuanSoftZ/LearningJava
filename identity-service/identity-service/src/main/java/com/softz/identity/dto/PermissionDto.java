package com.softz.identity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PermissionDto {
    private int id;
    private String name;
    private String description;
}
