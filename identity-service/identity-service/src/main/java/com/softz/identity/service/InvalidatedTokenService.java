package com.softz.identity.service;

import org.springframework.stereotype.Service;

import com.softz.identity.entity.InvalidatedToken;
import com.softz.identity.repository.InvalidateTokenRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvalidatedTokenService {
    InvalidateTokenRepository invalidatedTokenRepository;

    public InvalidatedToken create(InvalidatedToken invalidatedToken) {
        return invalidatedTokenRepository.save(invalidatedToken);
    }
}
