package com.rmntim.interfaces;

import com.rmntim.models.common.Order;
import com.rmntim.models.people.Group;
import com.rmntim.models.people.Person;

public interface CanOrder {
    String giveOrder(Person person, Order order);

    String giveOrder(Group<?> group, Order order);
}
