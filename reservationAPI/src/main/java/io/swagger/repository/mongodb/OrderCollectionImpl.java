package io.swagger.repository.mongodb;

import io.swagger.exceptions.OrderNotFoundException;
import io.swagger.model.document.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCollectionImpl implements OrderCollection {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Order findByOrderId(String orderId) {
        Query query = new Query(Criteria.where("_id").is(orderId));
        Order order = mongoTemplate.findOne(query, Order.class);
        if (order == null) {
            throw new OrderNotFoundException("Order not found");
        }
        return order;
    }

    @Override
    public void insertOrder(Order order) {
        mongoTemplate.insert(order);
    }
}
