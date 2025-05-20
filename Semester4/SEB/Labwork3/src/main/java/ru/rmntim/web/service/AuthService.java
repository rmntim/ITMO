package ru.rmntim.web.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.ContextResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import ru.rmntim.web.auth.JwtProvider;
import ru.rmntim.web.auth.PasswordHasher;
import ru.rmntim.web.auth.Role;
import ru.rmntim.web.dao.UserDAO;
import ru.rmntim.web.dto.UserInfoDTO;
import ru.rmntim.web.entity.UserEntity;
import ru.rmntim.web.exceptions.*;
import ru.rmntim.web.util.MessageUtil;

@Stateless
@Slf4j
public class AuthService {
    @EJB
    private UserDAO userDAO;

    @Inject
    private JwtProvider jwtProvider;

    @Inject
    private EmailService emailService;

    @Inject
    private MessageUtil messageUtil;

    @Context
    private ContextResolver<String> localeResolver;

    public Pair<String, UserInfoDTO> registerUser(String username, String password, String email)
            throws UserExistsException, ServerException, UserNotFoundException, InvalidEmailException {
        String language = localeResolver.getContext(String.class);
        if (userDAO.findByUsername(username).isPresent()) {
            throw new UserExistsException(messageUtil.getMessage("user.exists.with.username", language, username));
        }

        var newUser = UserEntity.builder()
                .username(username)
                .email(email)
                .password(PasswordHasher.hashPassword(password.toCharArray()))
                .role(Role.USER)
                .build();
        var createdUser = userDAO.createUser(newUser);

        log.info("Successfully added user: {}", createdUser);

        emailService.sendSignUpEmail(email, username);

        var token = jwtProvider.generateToken(createdUser.getUsername(), Role.USER, createdUser.getId(),
                createdUser.getEmail());
        userDAO.startNewSession(jwtProvider.getUserIdFromToken(token));

        return Pair.of(token, UserInfoDTO.fromEntity(createdUser));
    }

    public Pair<String, UserInfoDTO> authenticateUser(String email, String password)
            throws AuthenticationException, ServerException, UserNotFoundException {
        String language = localeResolver.getContext(String.class);
        var userOpt = userDAO.findByEmail(email);
        if (userOpt.isPresent()) {
            var user = userOpt.get();
            if (PasswordHasher.checkPassword(password.toCharArray(), user.getPassword())) {
                var token = jwtProvider.generateToken(user.getUsername(), Role.USER, user.getId(), user.getEmail());
                userDAO.startNewSession(jwtProvider.getUserIdFromToken(token));
                return Pair.of(token, UserInfoDTO.fromEntity(user));
            } else {
                throw new AuthenticationException(messageUtil.getMessage("user.wrong.credentials", language));
            }
        }
        throw new AuthenticationException(messageUtil.getMessage("user.no.email", language));
    }

    public void endSession(Long userId) throws UserNotFoundException {
        userDAO.endSession(userId);
    }
}
