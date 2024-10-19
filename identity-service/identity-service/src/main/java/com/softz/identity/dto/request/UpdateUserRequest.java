package com.softz.identity.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.softz.identity.validator.DobConstraint;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {
    private String password;
    @DobConstraint(min = 18, message = "INVALID_DATE_OF_BIRTH")
    private LocalDate dob;
    @Email(message = "INVALID_EMAIL")
    private String email;    
    List<Integer> roleIds;
}