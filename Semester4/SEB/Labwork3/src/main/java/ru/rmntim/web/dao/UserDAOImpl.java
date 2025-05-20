package ru.rmntim.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ru.rmntim.web.dto.UserInfoDTO;
import ru.rmntim.web.entity.UserEntity;
import ru.rmntim.web.entity.UserSessionEntity;
import ru.rmntim.web.exceptions.ServerException;
import ru.rmntim.web.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

@Stateless
public class UserDAOImpl implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        TypedQuery<UserEntity> query = entityManager
                .createQuery("SELECT u FROM UserEntity u WHERE u.username = :username", UserEntity.class);
        query.setParameter("username", username);
        return query.getResultStream().findFirst();
    }

    @Override
    public Optional<UserEntity> findById(long userId) {
        UserEntity user = entityManager.find(UserEntity.class, userId);
        return Optional.ofNullable(user);
    }

    @Override
    public UserEntity createUser(UserEntity user) throws ServerException {
        try {
            entityManager.persist(user);
            entityManager.flush();
            return user;
        } catch (Exception e) {
            throw new ServerException("Error creating User: ", e.getCause());
        }
    }

    @Override
    public void startNewSession(long userId) throws UserNotFoundException {
        // End any existing sessions that are past their expiry
        endExpiredSessions(userId);

        // Start new session
        UserEntity user = findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        LocalDateTime now = LocalDateTime.now();
        UserSessionEntity session = new UserSessionEntity(null, user, now, null, now);
        entityManager.persist(session);
    }

    private void endExpiredSessions(long userId) {
        LocalDateTime expiryTime = LocalDateTime.now().minusHours(1); // 1 hour session expiry
        entityManager.createQuery("UPDATE UserSessionEntity s SET s.sessionEnd = :now " +
                        "WHERE s.user.id = :userId AND s.sessionEnd IS NULL AND s.lastActivity < :expiryTime")
                .setParameter("now", LocalDateTime.now())
                .setParameter("userId", userId)
                .setParameter("expiryTime", expiryTime)
                .executeUpdate();
    }

    @Override
    public void endSession(long userId) throws UserNotFoundException {
        findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        UserSessionEntity lastSession = entityManager.createQuery(
                        "SELECT s FROM UserSessionEntity s WHERE s.user.id = :userId ORDER BY s.sessionStart DESC",
                        UserSessionEntity.class)
                .setParameter("userId", userId)
                .setMaxResults(1)
                .getSingleResult();

        lastSession.setSessionEnd(LocalDateTime.now());
        entityManager.merge(lastSession);
    }

    @Override
    public void updateLastActivity(long userId) {
        entityManager.createQuery(
                        "UPDATE UserSessionEntity s SET s.lastActivity = :now WHERE s.user.id = :userId AND s.sessionEnd IS NULL")
                .setParameter("now", LocalDateTime.now())
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        var query = entityManager
                .createQuery("SELECT u FROM UserEntity u WHERE u.email = :email", UserEntity.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }

    @Override
    public UserInfoDTO getUserInfo(long userId) throws UserNotFoundException {
        var user = findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        return UserInfoDTO.fromEntity(user);
    }

    @Override
    public UserInfoDTO updateUserInfo(long userId, UserInfoDTO userInfo) throws UserNotFoundException {
        var user = findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

        if (userInfo.getUsername() != null) {
            user.setUsername(userInfo.getUsername());
        }

        if (userInfo.getEmail() != null) {
            user.setEmail(userInfo.getEmail());
        }

        var newUser = entityManager.merge(user);

        return UserInfoDTO.fromEntity(newUser);
    }

    @Override
    public void deleteUser(long userId) throws UserNotFoundException {
        var user = findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        var query = entityManager
                .createQuery("DELETE FROM UserEntity u WHERE u.id = :userId");
        query.setParameter("userId", user.getId());
        query.executeUpdate();
    }

    @Override
    public void updatePassword(UserEntity user, String newPasswordHash) {
        user.setPassword(newPasswordHash);
        entityManager.merge(user);
    }

    @Override
    public UserInfoDTO updateAvatar(long userId, String avatarUrl) throws UserNotFoundException {
        var user = findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        user.setAvatarUrl(avatarUrl);
        var newUser = entityManager.merge(user);
        return UserInfoDTO.fromEntity(newUser);
    }

    @Override
    public UserInfoDTO getUserInfoById(long userId) throws UserNotFoundException {
        var user = findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        return UserInfoDTO.fromEntity(user);
    }
}
