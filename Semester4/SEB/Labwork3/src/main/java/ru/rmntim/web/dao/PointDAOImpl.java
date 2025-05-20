package ru.rmntim.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ru.rmntim.web.dto.PointDTO;
import ru.rmntim.web.entity.PointEntity;
import ru.rmntim.web.entity.UserEntity;
import ru.rmntim.web.exceptions.PointNotFoundException;
import ru.rmntim.web.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Stateless
public class PointDAOImpl implements PointDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PointEntity> getAll() {
        return entityManager.createQuery("SELECT p FROM PointEntity p", PointEntity.class).getResultList();
    }

    @Override
    public List<PointEntity> getPointsByUserId(Long userId) throws UserNotFoundException {
        findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        return entityManager.createQuery("SELECT p FROM PointEntity p WHERE p.user.id = :userId", PointEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public void addPointByUserId(Long userId, PointEntity point) throws UserNotFoundException {
        UserEntity user = findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        point.setUser(user);
        entityManager.persist(point);
    }

    @Override
    public void removePointByUserId(Long userId, PointDTO pointDTO)
            throws UserNotFoundException, PointNotFoundException {
        UserEntity user = findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));

        PointEntity pointToDelete = entityManager
                .createQuery(
                        "SELECT p FROM PointEntity p WHERE p.user = :user AND p.x = :x AND p.y = :y AND p.r = :r AND p.result = :result",
                        PointEntity.class)
                .setParameter("user", user)
                .setParameter("x", pointDTO.getX())
                .setParameter("y", pointDTO.getY())
                .setParameter("r", pointDTO.getR())
                .setParameter("result", pointDTO.isResult())
                .getResultStream()
                .findFirst()
                .orElseThrow(() -> new PointNotFoundException("Point not found"));

        entityManager.remove(pointToDelete);
    }

    @Override
    public void removeAllPointsByUserId(Long userId) throws UserNotFoundException {
        findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID " + userId + " not found."));
        entityManager.createQuery("DELETE FROM PointEntity p WHERE p.user.id = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        UserEntity user = entityManager.find(UserEntity.class, userId);
        return Optional.ofNullable(user);
    }
}
