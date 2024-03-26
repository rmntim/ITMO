package ru.rmntim.common.validators;

import ru.rmntim.common.exceptions.ValidationException;
import ru.rmntim.common.models.Color;
import ru.rmntim.common.models.Coordinates;
import ru.rmntim.common.models.Dragon;
import ru.rmntim.common.models.DragonCharacter;
import ru.rmntim.common.models.DragonType;

import java.time.ZonedDateTime;

public final class DragonValidator implements Validator<Dragon> {
    @Override
    public void validate(Dragon dragon) throws ValidationException {
        if (dragon == null) {
            throw new NullPointerException();
        }

        validateId(dragon.id());
        validateName(dragon.name());
        validateCoordinates(dragon.coordinates());
        validateCreationDate(dragon.creationDate());
        validateAge(dragon.age());
        validateColor(dragon.color());
        validateType(dragon.type());
        validateCharacter(dragon.character());
        if (dragon.head() != null) {
            new DragonHeadValidator().validate(dragon.head());
        }
    }

    private void validateId(Integer id) throws ValidationException {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid id");
        }
    }

    public void validateName(String name) throws ValidationException {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Invalid name");
        }
    }

    public void validateCoordinates(Coordinates coordinates) throws ValidationException {
        if (coordinates == null) {
            throw new ValidationException("Invalid coordinates");
        }
        new CoordinatesValidator().validate(coordinates);
    }

    public void validateCreationDate(ZonedDateTime creationDate) throws ValidationException {
        if (creationDate == null) {
            throw new ValidationException("Invalid creation date");
        }
    }

    public void validateAge(Long age) throws ValidationException {
        if (age == null || age <= 0) {
            throw new ValidationException("Invalid age");
        }
    }

    public void validateColor(Color color) throws ValidationException {
        if (color == null) {
            throw new ValidationException("Invalid color");
        }
    }

    public void validateType(DragonType type) throws ValidationException {
        if (type == null) {
            throw new ValidationException("Invalid type");
        }
    }

    public void validateCharacter(DragonCharacter character) throws ValidationException {
        if (character == null) {
            throw new ValidationException("Invalid character");
        }
    }
}
