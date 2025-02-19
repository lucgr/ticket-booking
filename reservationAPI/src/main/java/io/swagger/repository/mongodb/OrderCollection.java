package io.swagger.repository.mongodb;

import io.swagger.model.document.Order;

public interface OrderCollection {
    Order findByOrderId(String orderId);

    void insertOrder(Order order);
}
