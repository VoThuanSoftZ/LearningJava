package com.softz.identity.dto;

import java.time.LocalDate;
import java.util.List;

import com.softz.identity.entity.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String userId;
    private String username;
    private LocalDate dob;
    private String email;

    List<Role> roles;
}
