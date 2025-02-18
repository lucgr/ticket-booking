package io.swagger.repository;

import io.swagger.exceptions.EventNotFoundException;
import io.swagger.exceptions.OrderNotFoundException;
import io.swagger.exceptions.TicketAlreadyReservedException;
import io.swagger.exceptions.TicketNotFoundException;
import io.swagger.model.Ticket;
import io.swagger.model.TicketAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    private final Map<String, List<Ticket>> orderIdToTicketIds;
    private final Map<String, List<Ticket>> eventIdToTickets;

    @Autowired
    public ReservationRepositoryImpl() {
        this.orderIdToTicketIds = new HashMap<>();
        this.eventIdToTickets = new HashMap<>();
    }

    @Override
    public String createEvent(String eventName, int ticketsCount, float price) throws Exception {
        String eventId = java.util.UUID.randomUUID().toString();
        List<Ticket> tickets = new ArrayList<>();
        for (int i = 1; i <= ticketsCount; i++) {
            Ticket ticket = new Ticket()
                    .eventId(eventId)
                    .ticketId(String.valueOf(i))
                    .availability(TicketAvailability.AVAILABLE)
                    .price(price);
            tickets.add(ticket);
        }
        eventIdToTickets.put(eventId, tickets);
        return eventId;
    }

    @Override
    public List<Ticket> reserve(String orderId, String eventId, List<String> ticketIds) throws Exception {
        List<Ticket> reservedTickets = new ArrayList<>();
        if (!eventIdToTickets.containsKey(eventId)) {
            throw new EventNotFoundException("Event not found");
        }
        for (String ticketId : ticketIds) {
            Ticket ticket = eventIdToTickets.get(eventId).stream()
                    .filter(t -> t.getEventId().equals(eventId) && t.getTicketId().equals(ticketId))
                    .findFirst()
                    .orElseThrow(() -> new TicketNotFoundException("Ticket not found"));
            if (ticket.getAvailability() == TicketAvailability.RESERVED) {
                throw new TicketAlreadyReservedException("Ticket " + ticketId + " is already reserved");
            }
            reservedTickets.add(ticket);
        }
        for (Ticket ticket : reservedTickets) {
            ticket.setAvailability(TicketAvailability.RESERVED);
        }
        orderIdToTicketIds.put(orderId, reservedTickets);
        return reservedTickets;
    }

    public void release(String orderId) throws Exception {
        if (!orderIdToTicketIds.containsKey(orderId)) {
            throw new OrderNotFoundException("Order not found");
        }
        List<Ticket> reservedTickets = orderIdToTicketIds.get(orderId);
        if (reservedTickets != null) {
            for (Ticket ticket : reservedTickets) {
                ticket.setAvailability(TicketAvailability.AVAILABLE);
            }
            orderIdToTicketIds.remove(orderId);
        }
    }
}
