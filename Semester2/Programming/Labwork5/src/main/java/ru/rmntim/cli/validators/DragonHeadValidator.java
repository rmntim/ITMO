package ru.rmntim.cli.validators;

import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.models.DragonHead;

public final class DragonHeadValidator {
    private DragonHeadValidator() {
    }

    public static void validate(DragonHead head) throws ValidationException {
        validateEyesCount(head.eyesCount());
    }

    public static void validateEyesCount(Double eyesCount) throws ValidationException {
        if (eyesCount == null || eyesCount < 0) {
            throw new ValidationException("Invalid eyes count");
        }
    }
}
