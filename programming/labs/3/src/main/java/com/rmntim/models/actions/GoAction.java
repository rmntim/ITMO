package com.rmntim.models.actions;

import java.util.Objects;

public class GoAction extends Action {
    private final String destination;

    public GoAction(String destination) {
        super("отправляться");
        this.destination = destination;
    }

    @Override
    public String toString() {
        return getDescription() + " " + destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GoAction goAction = (GoAction) o;
        return Objects.equals(destination, goAction.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), destination);
    }
}
