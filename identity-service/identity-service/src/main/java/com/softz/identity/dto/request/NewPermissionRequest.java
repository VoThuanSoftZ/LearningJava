package com.softz.identity.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewPermissionRequest {
    private String name;
    private String description;
}