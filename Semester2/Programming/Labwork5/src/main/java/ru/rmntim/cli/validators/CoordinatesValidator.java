package ru.rmntim.cli.validators;

import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.models.Coordinates;

public final class CoordinatesValidator {
    private static final float MAX_X = 633;
    private static final float MIN_Y = -408;

    private CoordinatesValidator() {
    }

    public static void validate(Coordinates coordinates) throws ValidationException {
        validateX(coordinates.x());
        validateY(coordinates.y());
    }

    public static void validateX(Float x) throws ValidationException {
        if (x == null || x > MAX_X) {
            throw new ValidationException("X must be <= " + MAX_X);
        }
    }

    public static void validateY(float y) throws ValidationException {
        if (y <= MIN_Y) {
            throw new ValidationException("Y must be > " + MIN_Y);
        }
    }
}
