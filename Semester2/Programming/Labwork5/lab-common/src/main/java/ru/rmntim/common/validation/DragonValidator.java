package ru.rmntim.common.validation;

import ru.rmntim.common.models.Dragon;

import java.util.Objects;
import java.util.stream.Stream;

public class DragonValidator implements Validator<Dragon> {
    @Override
    public boolean validate(final Dragon dragon) {
        if (dragon.id() <= 0 || dragon.age() <= 0 || dragon.name().isEmpty()) {
            return false;
        }
        return Stream.of(dragon.id(), dragon.name(), dragon.coordinates(), dragon.creationDate(), dragon.age(), dragon.color(), dragon.type(), dragon.character(), dragon.head()).noneMatch(Objects::isNull);
    }
}
