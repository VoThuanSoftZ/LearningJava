package com.softz.identity.controller;

import org.springframework.web.bind.annotation.RestController;

import com.softz.identity.dto.AccessTokenDto;
import com.softz.identity.dto.ApiResponse;
import com.softz.identity.dto.request.AuthenticationRequest;
import com.softz.identity.service.coordinator.AuthenticationCoordinatorService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationCoordinatorService authenticationCoordinatorService;

    @PostMapping("/auth/token")
    public ApiResponse<AccessTokenDto> postMethodName(@RequestBody AuthenticationRequest request) {
        return ApiResponse.<AccessTokenDto>builder()
                .result(authenticationCoordinatorService
                        .authenticate(request))
                .build();
    }

}
