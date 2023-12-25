package com.rmntim.models.common;

import com.rmntim.models.actions.Action;

import java.util.List;
import java.util.Objects;

public class Order {
    private final String moment;
    private Communicator communicator;
    private final List<Action> actions;

    public Order(String moment, List<Action> actions) {
        this.moment = moment;
        this.actions = actions;
    }

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

    public boolean hasCommunicator() {
        return communicator != null;
    }

    public String getActions() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < actions.size(); i++) {
            if (i == 0) {
                builder.append(actions.get(i));
            } else if (i == actions.size() - 1) {
                builder.append(" и ").append(actions.get(i));
            } else {
                builder.append(", ").append(actions.get(i));
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
