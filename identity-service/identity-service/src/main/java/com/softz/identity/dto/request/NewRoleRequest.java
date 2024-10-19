package com.softz.identity.dto.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewRoleRequest {
    String name;
    String description;
    List<Integer> permissionIds;
}
