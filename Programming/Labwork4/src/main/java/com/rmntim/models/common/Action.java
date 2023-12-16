package com.rmntim.models.common;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public abstract class Action {
    private String description;

    Action(String description) {
        this.description = description;
    }

    public void setDescription(String description) {
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
        private final List<Item> items;

        public List<Item> getItems() {
            return items;
        }

        public Take(String item) {
            super(item);
            this.items = List.of(new Item(item, 0));
        }

        public Take(Item item) {
            super(item.name());
            this.items = List.of(item);
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
        private final List<Item> items;

        public List<Item> getItems() {
            return items;
        }

        public Attach(Item item, String destination) {
            super("к " + CaseConverter.toDative(destination) + " " + item.name());
            this.items = List.of(item);
        }

        public Attach(List<Item> items, String destination) {
            super("к" + CaseConverter.toDative(destination) + " ");

            var joiner = new StringJoiner(", ");

            for (var item : items)
                joiner.add(item.name());

            setDescription(joiner.toString());

            this.items = items;
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
        private final List<Item> items;

        public Hang(Item item, String destination) {
            super("на " + CaseConverter.toAccusative(destination) + " " + item.name());
            this.items = List.of(item);
        }

        public List<Item> getItems() {
            return items;
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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Action action = (Action) o;
        return Objects.equals(description, action.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }
}
