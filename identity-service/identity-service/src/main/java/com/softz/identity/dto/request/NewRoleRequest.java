package com.softz.identity.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewRoleRequest {
    private String name;
    private String description;
}