package ru.rmntim.cli.validators;

import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.models.DragonHead;

public final class DragonHeadValidator {
    private DragonHeadValidator() {
    }

    public static void validate(DragonHead head) throws ValidationException {
        if (head.eyesCount() == null || head.eyesCount() < 0) {
            throw new ValidationException("Invalid eyes count");
        }
    }
}
