package ru.rmntim.web.filters;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.auth.JwtProvider;
import ru.rmntim.web.auth.Role;
import ru.rmntim.web.auth.UserPrincipal;
import ru.rmntim.web.dto.ErrorDTO;
import ru.rmntim.web.service.PointService;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Provider
@Slf4j
@Priority(Priorities.AUTHORIZATION)
public class JwtAuthorizationFilter implements ContainerRequestFilter {
    @Inject
    private JwtProvider jwtProvider;

    @Inject
    private PointService userService;

    private static final Set<String> SKIP_PATHS = new HashSet<>(Arrays.asList(
            "/auth/signup",
            "/auth/login"));

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var path = requestContext.getUriInfo().getPath();
        if (SKIP_PATHS.contains(path)) {
            return; // Skip JWT check for specified paths
        }
        log.info(path);

        var authorizationCookie = requestContext.getCookies().get("token");
        if (authorizationCookie == null || authorizationCookie.getValue() == null
                || authorizationCookie.getValue().isBlank()) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(ErrorDTO.of("Authorization token is required"))
                    .build());
            return;
        }

        var token = authorizationCookie.getValue();

        if (jwtProvider.isTokenExpired(token)) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(ErrorDTO.of("Token expired"))
                    .build());
            return;
        }

        var username = jwtProvider.getUsernameFromToken(token);
        var role = jwtProvider.getRoleFromToken(token);
        var userId = jwtProvider.getUserIdFromToken(token);
        var email = jwtProvider.getEmailFromToken(token);

        if (username == null || role == null || userId == null || (email == null && role.equals(Role.USER))) {
            requestContext.abortWith(Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity(ErrorDTO.of("Invalid token"))
                    .build());
            return;
        }

        userService.updateLastActivity(userId);

        var originalContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return new UserPrincipal(username, userId, role, email);
            }

            @Override
            public boolean isUserInRole(String roleName) {
                return Optional.of(role)
                        .map(Enum::name)
                        .map(String::toUpperCase)
                        .map(roleName::equalsIgnoreCase)
                        .orElse(false);
            }

            @Override
            public boolean isSecure() {
                return originalContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        });
    }
}
