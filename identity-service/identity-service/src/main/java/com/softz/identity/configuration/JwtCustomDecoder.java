package com.softz.identity.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.SignedJWT;
import com.softz.identity.utils.JwtService;

@Component
public class JwtCustomDecoder implements JwtDecoder {
    @Autowired
    JwtService jwtService;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = jwtService.introspect(token);

            return new Jwt(
                    token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().toJSONObject());
        } catch (Exception exception) {
            throw new JwtException("Invalid token");
        }
    }
}
