package com.rmntim.models.common;

public enum Communicator {
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
}
