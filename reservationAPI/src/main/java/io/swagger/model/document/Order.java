package io.swagger.model.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Order {

    @Id
    private String orderId;

    private String userId;

    private String eventId;

    private List<Integer> ticketIds;

    public Order(String orderId, String userId, String eventId, List<Integer> ticketIds) {
        super();
        this.orderId = orderId;
        this.userId = userId;
        this.eventId = eventId;
        this.ticketIds = ticketIds;
    }

    public List<Integer> getTicketIds() {
        return ticketIds;
    }

    public String getEventId() {
        return eventId;
    }
}
