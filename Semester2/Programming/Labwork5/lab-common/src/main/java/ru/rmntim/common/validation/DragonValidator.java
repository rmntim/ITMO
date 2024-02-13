package ru.rmntim.common.validation;

import ru.rmntim.common.models.Dragon;

import java.util.Objects;
import java.util.stream.Stream;

public final class DragonValidator {
    private DragonValidator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean validate(final Dragon dragon) {
        var isValid = Stream.of(dragon.id(), dragon.name(), dragon.coordinates(), dragon.creationDate(), dragon.age(), dragon.color(), dragon.type(), dragon.character()).noneMatch(Objects::isNull);
        if (dragon.id() <= 0 || dragon.age() <= 0 || dragon.name().isEmpty()) {
            isValid = false;
        }
        if (dragon.coordinates() != null) {
            isValid = isValid && CoordinatesValidator.validate(dragon.coordinates());
        }
        if (dragon.head() != null) {
            isValid = isValid && DragonHeadValidator.validate(dragon.head());
        }
        return isValid;
    }
}
