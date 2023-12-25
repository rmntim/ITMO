package com.rmntim.interfaces;

import com.rmntim.models.common.Order;
import com.rmntim.models.people.Group;

public interface CanOrder {
    String giveOrder(Group<?> group, Order order);
}
