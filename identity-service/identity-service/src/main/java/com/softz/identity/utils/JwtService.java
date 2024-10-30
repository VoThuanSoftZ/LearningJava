package com.softz.identity.utils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

@Component
public class JwtService {
    @Value("${jwt.key}")
    private String tokenKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.ttl}")
    private long ttl;

    public boolean verifyToken(String token){
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier jwsVerifier = new MACVerifier(tokenKey);

            if(signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date())){
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }

            return signedJWT.verify(jwsVerifier);
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    public String generateToken(User user) {
        // Build header
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(user.getUsername())
                .claim("userId", user.getId())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plusSeconds(ttl).toEpochMilli()))
                .build();

        // Build payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Build token with header and payload
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        // Sign token
        JWSSigner jwsSigner;
        try {
            jwsSigner = new MACSigner(tokenKey);
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
