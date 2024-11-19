package com.softz.identity.configuration;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

public class CustomLocaleResolver extends AcceptHeaderLocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();

        // Check request params if contains 'lang' param
        if (!CollectionUtils.isEmpty(paramMap)
                && Objects.nonNull(paramMap.get("lang"))
                && paramMap.get("lang").length > 0) {

            // If contains 'lang' param then resolve Locale according to lang param
            String langParam = paramMap.get("lang")[0];
            return Locale.forLanguageTag(langParam);
        }

        // Otherwise delegate to AcceptHeaderLocaleResolver to resolve
        return super.resolveLocale(request);
    }
}
