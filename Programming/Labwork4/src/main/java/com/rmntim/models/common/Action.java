package com.rmntim.models.common;

import java.util.Objects;

public abstract class Action {
    private final String description;

    Action(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract String asSingular();

    public abstract String asInfinitive();

    public static class Go extends Action {
        public Go(String destination) {
            super(destination);
        }

        @Override
        public String asSingular() {
            return "отправился " + getDescription();
        }

        @Override
        public String asInfinitive() {
            return "отправляться " + getDescription();
        }
    }

    public static class Take extends Action {
        public Take(String subject) {
            super(subject);
        }

        @Override
        public String asSingular() {
            return "взял " + getDescription();
        }

        @Override
        public String asInfinitive() {
            return "взять с собой " + getDescription();
        }
    }

    public static class Attach extends Action {
        public Attach(String subject, String destination) {
            super("к " + CaseConverter.toDative(destination) + " " + subject);
        }

        @Override
        public String asSingular() {
            return "прицепил " + getDescription();
        }

        @Override
        public String asInfinitive() {
            return "прицепить " + getDescription();
        }
    }

    public static class Hang extends Action {
        public Hang(String subject, String destination) {
            super("на " + CaseConverter.toAccusative(destination) + " " + subject);
        }

        @Override
        public String asSingular() {
            return "подвесил " + getDescription();
        }

        @Override
        public String asInfinitive() {
            return "подвесить " + getDescription();
        }
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
