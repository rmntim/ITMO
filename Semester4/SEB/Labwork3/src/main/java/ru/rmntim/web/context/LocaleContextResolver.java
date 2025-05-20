package ru.rmntim.web.context;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.container.ContainerRequestContext;

@Provider
@RequestScoped
public class LocaleContextResolver implements ContextResolver<String> {
    @Context
    private ContainerRequestContext requestContext;

    @Override
    public String getContext(Class<?> type) {
        return (String) requestContext.getProperty("userLanguage");
    }
}