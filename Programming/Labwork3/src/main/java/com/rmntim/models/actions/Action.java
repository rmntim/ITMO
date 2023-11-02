package com.rmntim.models.actions;

import java.util.Objects;

public abstract class Action {
    private final String description;

    public Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(description, action.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
