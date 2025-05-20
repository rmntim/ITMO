package ru.rmntim.web.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.security.Principal;

@Data
@Builder
@AllArgsConstructor
public class UserPrincipal implements Principal {
    private final String name;
    private final Long userId;
    private final Role role;
    private final String email;
}