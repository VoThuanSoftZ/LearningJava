package com.softz.identity.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPermissionRequest {
    private String name;
    private String description;
}