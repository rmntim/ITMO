package com.rmntim.models.actions;

import java.util.Objects;

public class TakeAction extends Action {
    private final String subject;

    public TakeAction(String subject) {
        super("взять с собой");
        this.subject = subject;
    }

    @Override
    public String toString() {
        return getDescription() + " " + subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TakeAction that = (TakeAction) o;
        return Objects.equals(subject, that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject);
    }
}
