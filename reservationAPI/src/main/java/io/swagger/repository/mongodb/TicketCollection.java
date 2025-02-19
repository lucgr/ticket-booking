package io.swagger.repository.mongodb;

import io.swagger.model.document.Ticket;

import java.util.List;

public interface TicketCollection {
    List<Ticket> findByTicketIds(String eventId, List<Integer> ticketIds);

    void updateTicketAvailability(String eventId, List<Integer> ticketIds, String availability);

    void insertTickets(List<Ticket> tickets);
}
