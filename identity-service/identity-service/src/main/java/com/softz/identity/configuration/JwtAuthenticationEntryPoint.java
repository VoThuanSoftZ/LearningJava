package com.softz.identity.configuration;

import java.io.IOException;
import java.util.Locale;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softz.identity.dto.ApiResponse;
import com.softz.identity.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MessageResource messageResource;

    private String getErrorMessage(ErrorCode errorCode, String... params) {
        Locale requestLocale = LocaleContextHolder.getLocale();

        try {
            return messageResource.getMessage(errorCode.name(), requestLocale, params);
            // This exception happen when message key is not existed
        } catch (NoSuchMessageException e) {
            log.error("Unable to find message for {} in language: {}", errorCode.name(), requestLocale.getLanguage());
            return messageResource.getMessage(
                    ErrorCode.UNCATEGORIZED_EXCEPTION.name(), requestLocale, errorCode.name());
        }
    }

    @Override
    public void commence(
            HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(getErrorMessage(errorCode))
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
