package ru.rmntim.web.dao;

import ru.rmntim.web.dto.UserInfoDTO;
import ru.rmntim.web.entity.UserEntity;
import ru.rmntim.web.exceptions.ServerException;
import ru.rmntim.web.exceptions.UserNotFoundException;

import java.util.Optional;

public interface UserDAO {
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(long userId);

    UserEntity createUser(UserEntity user) throws ServerException;

    void startNewSession(long userId) throws UserNotFoundException;

    void endSession(long userId) throws UserNotFoundException;

    void updateLastActivity(long userId);

    Optional<UserEntity> findByEmail(String email);

    UserInfoDTO getUserInfo(long userId) throws UserNotFoundException;

    UserInfoDTO updateUserInfo(long userId, UserInfoDTO userInfo) throws UserNotFoundException;

    void deleteUser(long userId) throws UserNotFoundException;

    void updatePassword(UserEntity user, String newPasswordHash);

    UserInfoDTO updateAvatar(long userId, String avatarUrl) throws UserNotFoundException;

    UserInfoDTO getUserInfoById(long userId) throws UserNotFoundException;
}
