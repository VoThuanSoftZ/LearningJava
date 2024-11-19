package com.softz.identity.utils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.softz.identity.entity.User;
import com.softz.identity.exception.AppException;
import com.softz.identity.exception.ErrorCode;
import com.softz.identity.repository.InvalidateTokenRepository;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtService {
    @NonFinal
    @Value("${jwt.key}")
    private String tokenKey;

    @NonFinal
    @Value("${jwt.issuer}")
    private String issuer;

    InvalidateTokenRepository invalidateTokenRepository;
    //todo
    public TokenInfo extractToken(String token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier jwsVerifier = new MACVerifier(tokenKey.getBytes());

            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                return null;
            }

            if (signedJWT.verify(jwsVerifier)) {
                return null;
            }
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String userId = claims.getStringClaim("userId");
            return new TokenInfo(
                    signedJWT.getJWTClaimsSet().getJWTID(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant());
        } catch (ParseException | JOSEException e) {
            return null;
        }
    }

    public String getUserIdFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier jwsVerifier = new MACVerifier(tokenKey.getBytes());

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
            String userId = claims.getStringClaim("userId");
            return userId;
        } catch (ParseException | JOSEException e) {
            return null;
        }
    }

    public SignedJWT introspect(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier jwsVerifier = new MACVerifier(tokenKey.getBytes());

            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            String jit = signedJWT.getJWTClaimsSet().getJWTID();
            if (invalidateTokenRepository.existsById(jit)) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            if (!signedJWT.verify(jwsVerifier)) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            return signedJWT;
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public record TokenInfo(String jti, Instant expiryTime) {}

    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .jwtID(UUID.randomUUID().toString())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(3600, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        JWSSigner jwsSigner;
        try {
            jwsSigner = new MACSigner(tokenKey.getBytes());
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyToken(String token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            JWSVerifier jwsVerifier = new MACVerifier(tokenKey.getBytes());

            if (signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            return signedJWT.verify(jwsVerifier);
        } catch (ParseException | JOSEException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private String buildScope(User user) {
        if (CollectionUtils.isEmpty(user.getRoles())) return "";

        StringJoiner stringJoiner = new StringJoiner(" ");

        user.getRoles().forEach(role -> {
            stringJoiner.add(String.format("ROLE_%s", role.getName()));
            if (!CollectionUtils.isEmpty(role.getPermissions())) {
                role.getPermissions()
                        .forEach(permission -> stringJoiner.add(String.format("PERMISSION_%s", permission.getName())));
            }
        });

        return stringJoiner.toString();
    }
}
