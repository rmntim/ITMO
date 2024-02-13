package ru.rmntim.server.validation;

import ru.rmntim.common.models.Dragon;

import java.util.Objects;
import java.util.stream.Stream;

public class DragonValidator implements Validator<Dragon> {
    @Override
    public boolean validate(final Dragon dragon) {
        var isValid = Stream.of(dragon.id(), dragon.name(), dragon.coordinates(), dragon.creationDate(), dragon.age(), dragon.color(), dragon.type(), dragon.character()).noneMatch(Objects::isNull);
        if (dragon.id() <= 0 || dragon.age() <= 0 || dragon.name().isEmpty()) {
            isValid = false;
        }
        if (dragon.coordinates() != null) {
            isValid = isValid && new CoordinatesValidator().validate(dragon.coordinates());
        }
        if (dragon.head() != null) {
            isValid = isValid && new DragonHeadValidator().validate(dragon.head());
        }
        return isValid;
    }
}
