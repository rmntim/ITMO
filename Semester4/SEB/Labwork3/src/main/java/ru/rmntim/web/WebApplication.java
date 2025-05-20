package ru.rmntim.web;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import ru.rmntim.web.context.LocaleContextResolver;
import ru.rmntim.web.filters.LocaleFilter;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api/v1")
public class WebApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // Register providers
        classes.add(LocaleContextResolver.class);
        classes.add(LocaleFilter.class);
        return classes;
    }
}