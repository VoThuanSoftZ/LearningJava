package com.softz.identity.service.coordinator;

import java.util.Objects;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.softz.identity.dto.AccessTokenDto;
import com.softz.identity.dto.request.AuthenticationRequest;
import com.softz.identity.dto.request.InvalidateTokenRequest;
import com.softz.identity.entity.InvalidatedToken;
import com.softz.identity.entity.User;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.service.InvalidatedTokenService;
import com.softz.identity.service.UserService;
import com.softz.identity.utils.JwtService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationCoordinatorService {
    UserService userService;
    JwtService jwtService;
    InvalidatedTokenService invalidatedTokenService;

    public AccessTokenDto authenticate(AuthenticationRequest request) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User user;
        try {            
            user = userService.getUserByUsername(request.getUsername());
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (!bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String token = jwtService.generateToken(user);
        return AccessTokenDto.builder()
                .token(token)
                .build();
    }

    public void invalidate(InvalidateTokenRequest request) {
        JwtService.TokenInfo tokenInfo = jwtService.extractToken(request.getToken());

        if (Objects.nonNull(tokenInfo)) {
            InvalidatedToken invalidateToken = InvalidatedToken.builder()
                    .id(tokenInfo.jti())
                    .expiry(tokenInfo.expiryTime())
                    .build();

            invalidatedTokenService.create(invalidateToken);
        }
    }
}
