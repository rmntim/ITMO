package ru.rmntim.cli.validators;

import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.models.Coordinates;

public final class CoordinatesValidator implements Validator<Coordinates> {
    private static final float MAX_X = 633;
    private static final float MIN_Y = -408;

    @Override
    public void validate(Coordinates coordinates) throws ValidationException {
        if (coordinates == null) {
            throw new NullPointerException();
        }

        validateX(coordinates.x());
        validateY(coordinates.y());
    }

    public void validateX(Float x) throws ValidationException {
        if (x == null || x > MAX_X) {
            throw new ValidationException("X must be <= " + MAX_X);
        }
    }

    public void validateY(float y) throws ValidationException {
        if (y <= MIN_Y) {
            throw new ValidationException("Y must be > " + MIN_Y);
        }
    }
}
