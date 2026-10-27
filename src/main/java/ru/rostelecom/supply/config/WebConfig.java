package ru.rostelecom.supply.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import java.util.Locale;

@Configuration
public class WebConfig {

    /** Интерфейс всегда на русском: даты и числа форматируются по-русски. */
    @Bean
    public LocaleResolver localeResolver() {
        return new FixedLocaleResolver(Locale.forLanguageTag("ru-RU"));
    }
}
