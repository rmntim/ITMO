package ru.rmntim.common.validators;

import ru.rmntim.common.models.DragonHead;

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
