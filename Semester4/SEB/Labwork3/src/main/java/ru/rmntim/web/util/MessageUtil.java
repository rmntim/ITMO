package ru.rmntim.web.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import ru.rmntim.web.config.LocaleConfig;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@ApplicationScoped
public class MessageUtil {
    @Inject
    private LocaleConfig localeConfig;

    public String getMessage(String key, String language, Object... params) {
        ResourceBundle bundle = localeConfig.getResourceBundle(language);
        String message = bundle.getString(key);

        if (params != null && params.length > 0) {
            return MessageFormat.format(message, params);
        }

        return message;
    }

    public String getMessage(String key, String language) {
        return getMessage(key, language, (Object[]) null);
    }
}