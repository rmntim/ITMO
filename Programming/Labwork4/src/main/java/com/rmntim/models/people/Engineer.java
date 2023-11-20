package com.rmntim.models.people;

public class Engineer extends Person {
    public Engineer(String name, Sex sex) {
        super(name, sex);
    }

    public String dativeCase() {
        if (getName().matches(".*(?i)[аеёоуиэя]")) {
            return "инженеру " + getName().substring(0, getName().length() - 1) + "е";
        }

        return "инженеру " + getName() + "у";
    }
}
