package ru.rmntim.web.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.exceptions.ConfigurationException;
import ru.rmntim.web.exceptions.ServerException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@ApplicationScoped
public class JwtProvider {
    @Resource(lookup = "java:global/jwt/secret")
    private String SECRET;

    public String generateToken(String username, Role role, Long userId, String email) throws ServerException {
        try {
            return JWT.create()
                    .withSubject(username)
                    .withClaim("userId", userId)
                    .withClaim("role", role.toString())
                    .withClaim("email", email)
                    .withExpiresAt(Instant.now().plus(30, ChronoUnit.MINUTES))
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (ConfigurationException e) {
            log.error("Error generating token: {}", e.getMessage());
            throw new ServerException("Internal server error.", e);
        }
    }

    public String getEmailFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("email").asString();
        } catch (JWTDecodeException exception) {
            log.error("Error decoding email from token: {}", exception.getMessage());
            return null;
        }
    }

    public String getUsernameFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getSubject();
        } catch (JWTDecodeException exception) {
            log.error("Error decoding username from token: {}", exception.getMessage());
            return null;
        }
    }

    public Role getRoleFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String roleStr = jwt.getClaim("role").asString();
            return Role.valueOf(roleStr);
        } catch (JWTDecodeException | IllegalArgumentException exception) {
            log.error("Error decoding role from token: {}", exception.getMessage());
            return null;
        }
    }

    public Long getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        } catch (JWTDecodeException exception) {
            log.error("Error decoding user id from token: {}", exception.getMessage());
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expirationTime = jwt.getExpiresAt();
            return expirationTime != null && expirationTime.before(new Date());
        } catch (JWTDecodeException exception) {
            log.error("Error decoding expiration from token: {}", exception.getMessage());
            return true;
        }
    }
}
