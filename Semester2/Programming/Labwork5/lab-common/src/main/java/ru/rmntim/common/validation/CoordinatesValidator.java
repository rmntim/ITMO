package ru.rmntim.common.validation;

import ru.rmntim.common.models.Coordinates;

public final class CoordinatesValidator {
    private static final float MAX_X = 633;
    private static final float MIN_Y = -408;

    private CoordinatesValidator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean validate(final Coordinates coordinates) {
        return coordinates.x() != null && !(coordinates.x() > MAX_X) && !(coordinates.y() <= MIN_Y);
    }
}
