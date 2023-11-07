package com.rmntim.models.common;

import com.rmntim.interfaces.HasCase;

public enum Communicator implements HasCase {
    RADIOPHONE("радиотелефон"),
    PAGER("пейджер"),
    IPHONE_15_PRO("телефон");

    private final String name;

    Communicator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String dativeCase() {
        if (getName().matches(".*(?i)[аеёоуиэя]")) {
            return getName().substring(0, getName().length() - 1) + "е";
        }

        return getName() + "у";
    }
}
