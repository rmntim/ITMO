package com.rmntim.models.people;

import com.rmntim.interfaces.CanOrder;
import com.rmntim.models.common.CaseConverter;
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
        joiner.add("по " + CaseConverter.toDative(order.getCommunicator().getName()));
        joiner.add("приказ");
        joiner.add(CaseConverter.toDative(group));
        joiner.add(order.getActions());
        return joiner.toString();
    }
}
