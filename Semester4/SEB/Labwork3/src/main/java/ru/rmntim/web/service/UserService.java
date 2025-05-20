package ru.rmntim.web.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.ContextResolver;
import lombok.extern.slf4j.Slf4j;
import ru.rmntim.web.auth.PasswordHasher;
import ru.rmntim.web.dao.UserDAO;
import ru.rmntim.web.dto.UserInfoDTO;
import ru.rmntim.web.exceptions.UserNotFoundException;
import ru.rmntim.web.util.MessageUtil;

import java.io.InputStream;

@Stateless
@Slf4j
public class UserService {
    @EJB
    private UserDAO userDAO;

    @Inject
    private EmailService emailService;

    @Inject
    private AvatarService avatarService;

    @Inject
    private MessageUtil messageUtil;

    @Context
    private ContextResolver<String> localeResolver;

    public UserInfoDTO getUserInfo(long userId) throws UserNotFoundException {
        String language = localeResolver.getContext(String.class);
        return userDAO.getUserInfo(userId);
    }

    public UserInfoDTO updateUserInfo(long userId, UserInfoDTO userInfo) throws UserNotFoundException {
        String language = localeResolver.getContext(String.class);
        return userDAO.updateUserInfo(userId, userInfo);
    }

    public void deleteUser(long userId) throws UserNotFoundException {
        String language = localeResolver.getContext(String.class);
        userDAO.deleteUser(userId);
    }

    public void updatePassword(long userId, String currentPassword, String newPassword) throws UserNotFoundException {
        String language = localeResolver.getContext(String.class);
        var user = userDAO.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(messageUtil.getMessage("user.not.found", language)));

        if (!PasswordHasher.checkPassword(currentPassword.toCharArray(), user.getPassword())) {
            throw new UserNotFoundException(messageUtil.getMessage("user.wrong.password", language));
        }

        var newPasswordHash = PasswordHasher.hashPassword(newPassword.toCharArray());
        userDAO.updatePassword(user, newPasswordHash);

        emailService.sendPasswordChangeEmail(user.getEmail());
    }

    public UserInfoDTO uploadAvatar(long userId, InputStream inputStream, MediaType mediaType)
            throws UserNotFoundException {
        String language = localeResolver.getContext(String.class);
        var avatarUrl = avatarService.uploadAvatar(inputStream, mediaType);
        return userDAO.updateAvatar(userId, avatarUrl);
    }

    public UserInfoDTO getUserInfoById(long userId) throws UserNotFoundException {
        String language = localeResolver.getContext(String.class);
        return userDAO.getUserInfoById(userId);
    }
}
