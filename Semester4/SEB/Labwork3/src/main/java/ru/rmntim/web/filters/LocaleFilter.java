package ru.rmntim.web.filters;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

@Provider
@PreMatching
@Priority(1)
public class LocaleFilter implements ContainerRequestFilter {
    private static final String DEFAULT_LANG = "en";

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String acceptLanguage = requestContext.getHeaderString("Accept-Language");
        String lang = DEFAULT_LANG;

        if (acceptLanguage != null && !acceptLanguage.isEmpty()) {
            String primaryLang = acceptLanguage.split(",")[0].trim().split("-")[0].toLowerCase();
            if (primaryLang.equals("ru")) {
                lang = "ru";
            }
        }

        requestContext.setProperty("userLanguage", lang);
    }
}