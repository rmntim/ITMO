package com.rmntim.models.common;

import com.rmntim.interfaces.HasCase;

import java.util.Objects;

public record Communicator(String name) implements HasCase {

    @Override
    public String dativeCase() {
        if (name().matches(".*(?i)[аеёоуиэя]")) {
            return name().substring(0, name().length() - 1) + "е";
        }

        return name() + "у";
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Communicator that = (Communicator) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
