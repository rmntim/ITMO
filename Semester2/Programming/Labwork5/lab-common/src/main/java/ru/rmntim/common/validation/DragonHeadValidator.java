package ru.rmntim.common.validation;

import ru.rmntim.common.models.DragonHead;

public final class DragonHeadValidator {
    private DragonHeadValidator() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean validate(final DragonHead head) {
        return head.eyesCount() != null;
    }
}
