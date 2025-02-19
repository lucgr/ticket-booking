package io.swagger.repository;

import io.swagger.model.Ticket;

import java.util.List;

public interface ReservationRepository {
    String createEvent(String eventName, int ticketsCount, float price) throws Exception;

    List<Ticket> reserve(String orderId, String userId, String eventId, List<Integer> ticketIds) throws Exception;

    void release(String orderId) throws Exception;
}
