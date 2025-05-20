package ru.rmntim.web.dao;

import ru.rmntim.web.dto.PointDTO;
import ru.rmntim.web.entity.PointEntity;
import ru.rmntim.web.entity.UserEntity;
import ru.rmntim.web.exceptions.PointNotFoundException;
import ru.rmntim.web.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

public interface PointDAO {
    List<PointEntity> getAll();

    List<PointEntity> getPointsByUserId(Long userId) throws UserNotFoundException;

    void addPointByUserId(Long userId, PointEntity point) throws UserNotFoundException;

    void removePointByUserId(Long userId, PointDTO pointDTO) throws UserNotFoundException, PointNotFoundException;

    void removeAllPointsByUserId(Long userId) throws UserNotFoundException;

    Optional<UserEntity> findById(Long userId);
}
