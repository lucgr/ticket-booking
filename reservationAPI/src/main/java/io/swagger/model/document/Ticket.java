package io.swagger.model.document;

import io.swagger.model.TicketAvailability;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Ticket {

    private String eventId;
    private Integer ticketId;
    private String availability;
    private float price;

    public Ticket(Integer ticketId, String eventId, String availability, float price) {
        super();
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.availability = availability;
        this.price = price;
    }


    public boolean isAvailable() {
        return availability.equals(TicketAvailability.AVAILABLE.toString());
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public String getEventId() {
        return eventId;
    }

    public String getAvailability() {
        return availability;
    }

    public float getPrice() {
        return price;
    }
}
