package com.rmntim.models.people;

public enum Sex {
    MALE("он"),
    FEMALE("она");

    private final String pronoun;

    Sex(String pronoun) {
        this.pronoun = pronoun;
    }

    public String getPronoun() {
        return pronoun;
    }

    @Override
    public String toString() {
        return pronoun;
    }
}
