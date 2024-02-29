package ru.rmntim.cli.validators;

import ru.rmntim.cli.exceptions.ValidationException;
import ru.rmntim.cli.models.DragonHead;

public final class DragonHeadValidator implements Validator<DragonHead> {
    @Override
    public void validate(DragonHead head) throws ValidationException {
        if (head == null) {
            throw new NullPointerException();
        }

        validateEyesCount(head.eyesCount());
    }

    public void validateEyesCount(Double eyesCount) throws ValidationException {
        if (eyesCount == null || eyesCount < 0) {
            throw new ValidationException("Invalid eyes count");
        }
    }
}
