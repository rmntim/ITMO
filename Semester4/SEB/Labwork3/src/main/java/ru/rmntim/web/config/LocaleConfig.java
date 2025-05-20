package ru.rmntim.web.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.util.Locale;
import java.util.ResourceBundle;

@ApplicationScoped
public class LocaleConfig {
    private static final String MESSAGES_BUNDLE = "messages";
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    private static final Locale RUSSIAN_LOCALE = Locale.of("ru");

    @Produces
    @ApplicationScoped
    public ResourceBundle getDefaultResourceBundle() {
        return ResourceBundle.getBundle(MESSAGES_BUNDLE, DEFAULT_LOCALE);
    }

    public ResourceBundle getResourceBundle(String language) {
        Locale locale = language != null && language.equals("ru") ? RUSSIAN_LOCALE : DEFAULT_LOCALE;
        return ResourceBundle.getBundle(MESSAGES_BUNDLE, locale);
    }
}