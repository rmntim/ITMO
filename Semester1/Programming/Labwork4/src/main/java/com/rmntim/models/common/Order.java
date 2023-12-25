package com.rmntim.models.common;

import java.util.List;
import java.util.Objects;

public class Order {
    private final String moment;
    private final Communicator communicator;
    private final List<Action> actions;

    public Order(String moment, Communicator communicator, List<Action> actions) {
        this.moment = moment;
        this.communicator = communicator;
        this.actions = actions;
    }

    public String getMoment() {
        return moment;
    }

    public Communicator getCommunicator() {
        return communicator;
    }

    public String getActions() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < actions.size(); i++) {
            var action = actions.get(i).asInfinitive();
            if (i == 0) {
                builder.append(action);
            } else if (i == actions.size() - 1) {
                builder.append(" и ").append(action);
            } else {
                builder.append(", ").append(action);
            }
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(moment, order.moment) &&
                Objects.equals(communicator, order.communicator) &&
                Objects.equals(actions, order.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moment, communicator, actions);
    }
}
