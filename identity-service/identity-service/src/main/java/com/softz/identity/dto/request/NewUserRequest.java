package com.softz.identity.dto.request;

import java.time.LocalDate;

import com.softz.identity.validator.DobConstraint;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewUserRequest {
    @Size(min = 3, max = 31, message = "INVALID_USERNAME")
    private String username;
    private String password;
    @DobConstraint(min = 18, message = "INVALID_DATE_OF_BIRTH")
    private LocalDate dob;
}