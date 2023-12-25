package com.rmntim.exceptions;

import com.rmntim.models.common.Item;

public class NotEnoughSpaceException extends Exception {
    public NotEnoughSpaceException(Item item) {
        super("Item " + item.name() + "(" + item.weight() + " kg) does not fit.");
    }
}
