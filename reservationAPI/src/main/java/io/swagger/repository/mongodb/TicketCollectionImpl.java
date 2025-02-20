package io.swagger.repository.mongodb;

import io.swagger.model.document.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TicketCollectionImpl implements TicketCollection {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Ticket> findByTicketIds(String eventId, List<Integer> ticketIds) {
        Query query = new Query(Criteria.where("eventId").is(eventId).and("ticketId").in(ticketIds));
        return mongoTemplate.find(query, Ticket.class);
    }

    @Override
    public void updateTicketAvailability(String eventId, List<Integer> ticketIds, String availability) {
        Query query = new Query(Criteria.where("eventId").is(eventId).and("ticketId").in(ticketIds));
        Update update = new Update().set("availability", availability);
        mongoTemplate.updateMulti(query, update, Ticket.class);
    }

    @Override
    public void insertTickets(List<Ticket> tickets) {
        mongoTemplate.insert(tickets, Ticket.class);
    }
}
