package io.swagger.repository;

import io.swagger.exceptions.TicketAlreadyReservedException;
import io.swagger.exceptions.TicketNotFoundException;
import io.swagger.model.Ticket;
import io.swagger.model.TicketAvailability;
import io.swagger.model.document.Order;
import io.swagger.repository.mongodb.OrderCollection;
import io.swagger.repository.mongodb.TicketCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {

    @Autowired
    private OrderCollection orderCollection;

    @Autowired
    private TicketCollection ticketCollection;

    @Override
    public String createEvent(String eventName, int ticketsCount, float price) {
        String eventId = java.util.UUID.randomUUID().toString();
        List<io.swagger.model.document.Ticket> tickets = new ArrayList<>();
        for (int ticketId = 1; ticketId <= ticketsCount; ticketId++) {
            io.swagger.model.document.Ticket ticket = new io.swagger.model.document.Ticket(
                    ticketId,
                    eventId,
                    TicketAvailability.AVAILABLE.toString(),
                    price
            );
            tickets.add(ticket);
        }
        ticketCollection.insertTickets(tickets);
        return eventId;
    }

    @Override
    @Transactional
    public List<Ticket> reserve(String orderId, String userId, String eventId, List<Integer> ticketIds) {
        List<io.swagger.model.document.Ticket> availableTickets = ticketCollection.findByTicketIds(eventId, ticketIds);
        if (availableTickets.size() != ticketIds.size()) {
            throw new TicketNotFoundException("Ticket not found");
        }
        for (io.swagger.model.document.Ticket ticket : availableTickets) {
            if (!ticket.isAvailable()) {
                throw new TicketAlreadyReservedException("Ticket " + ticket.getTicketId() + " is already reserved");
            }
        }
        ticketCollection.updateTicketAvailability(eventId, ticketIds, TicketAvailability.RESERVED.toString());
        orderCollection.insertOrder(new io.swagger.model.document.Order(orderId, userId, eventId, ticketIds));

        List<Ticket> responseTickets = new ArrayList<>();
        for (io.swagger.model.document.Ticket ticket : availableTickets) {
            responseTickets.add(new io.swagger.model.Ticket()
                    .ticketId(ticket.getTicketId())
                    .eventId(ticket.getEventId())
                    .availability(TicketAvailability.RESERVED)
                    .price(ticket.getPrice())
            );
        }
        return responseTickets;
    }

    @Transactional
    public void release(String orderId) {
        Order order = orderCollection.findByOrderId(orderId);
        ticketCollection.updateTicketAvailability(order.getEventId(), order.getTicketIds(), TicketAvailability.AVAILABLE.toString());
    }
}
