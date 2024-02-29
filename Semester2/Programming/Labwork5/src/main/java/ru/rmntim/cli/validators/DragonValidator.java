package ru.rmntim.cli.validators;

import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.models.Color;
import ru.rmntim.cli.models.Coordinates;
import ru.rmntim.cli.models.Dragon;
import ru.rmntim.cli.models.DragonCharacter;
import ru.rmntim.cli.models.DragonType;

import java.time.ZonedDateTime;

public final class DragonValidator {
    private DragonValidator() {
    }

    public static void validate(Dragon dragon) throws ValidationException {
        validateId(dragon.id());
        validateName(dragon.name());
        validateCoordinates(dragon.coordinates());
        validateCreationDate(dragon.creationDate());
        validateAge(dragon.age());
        validateColor(dragon.color());
        validateType(dragon.type());
        validateCharacter(dragon.character());
        if (dragon.head() != null) {
            DragonHeadValidator.validate(dragon.head());
        }
    }

    public static void validateId(Integer id) throws ValidationException {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid id");
        }
    }

    public static void validateName(String name) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Invalid name");
        }
    }

    public static void validateCoordinates(Coordinates coordinates) throws ValidationException {
        if (coordinates == null) {
            throw new ValidationException("Invalid coordinates");
        }
        CoordinatesValidator.validate(coordinates);
    }

    public static void validateCreationDate(ZonedDateTime creationDate) throws ValidationException {
        if (creationDate == null) {
            throw new ValidationException("Invalid creation date");
        }
    }

    public static void validateAge(Long age) throws ValidationException {
        if (age == null || age <= 0) {
            throw new ValidationException("Invalid age");
        }
    }

    public static void validateColor(Color color) throws ValidationException {
        if (color == null) {
            throw new ValidationException("Invalid color");
        }
    }

    public static void validateType(DragonType type) throws ValidationException {
        if (type == null) {
            throw new ValidationException("Invalid type");
        }
    }

    public static void validateCharacter(DragonCharacter character) throws ValidationException {
        if (character == null) {
            throw new ValidationException("Invalid character");
        }
    }
}
