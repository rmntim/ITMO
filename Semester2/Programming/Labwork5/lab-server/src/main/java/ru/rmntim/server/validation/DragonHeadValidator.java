package ru.rmntim.server.validation;

import ru.rmntim.common.models.DragonHead;

public class DragonHeadValidator implements Validator<DragonHead> {
    @Override
    public boolean validate(final DragonHead head) {
        return head.eyesCount() != null;
    }
}
