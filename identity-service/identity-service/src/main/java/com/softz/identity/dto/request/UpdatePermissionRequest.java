package com.softz.identity.dto.request;

import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePermissionRequest {

    @Size(min = 5, max = 30, message = "INVALID_FIELD")
    private String name;

    private String description;
}
