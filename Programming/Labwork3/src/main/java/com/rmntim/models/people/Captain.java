package com.rmntim.models.people;

import com.rmntim.interfaces.CanOrder;
import com.rmntim.models.common.Order;

import java.util.StringJoiner;

public class Captain extends Person implements CanOrder {
    public Captain(String name, Sex sex) {
        super(name, sex);
    }

    @Override
    public String giveOrder(Group<?> group, Order order) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(sex.getPronoun());
        joiner.add(order.getMoment());
        joiner.add(isMale() ? "отдал" : "отдала");
        if (order.hasCommunicator())
            joiner.add("по " + order.getCommunicator().dativeCase());
        joiner.add("приказ");
        joiner.add(group.dativeCase());
        joiner.add(order.getActions());
        return joiner.toString();
    }

    @Override
    public String giveOrder(Person person, Order order) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(sex.getPronoun());
        joiner.add(order.getMoment());
        joiner.add(isMale() ? "отдал" : "отдала");
        if (order.hasCommunicator())
            joiner.add("по " + order.getCommunicator().dativeCase());
        joiner.add("приказ");
        joiner.add(person.dativeCase());
        joiner.add(order.getActions());
        return joiner + ".";
    }
}
