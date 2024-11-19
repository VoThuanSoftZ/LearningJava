package com.softz.identity.configuration;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

@Component
@Configuration
public class MessageResource {

    private static final String LOCALE_LANGUAGE = "en";

    public String getMessage(String key, String... params) {
        return messageSource().getMessage(key, params, LocaleContextHolder.getLocale());
    }

    public String getMessage(String key, Locale local, String... params) {
        return messageSource().getMessage(key, params, local);
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.addBasenames("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        Locale locale = Locale.forLanguageTag(LOCALE_LANGUAGE);

        messageSource.setDefaultLocale(locale);

        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }
}
