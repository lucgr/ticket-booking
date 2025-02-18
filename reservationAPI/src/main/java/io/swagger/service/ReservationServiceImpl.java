package io.swagger.service;

import io.swagger.exceptions.EventNotFoundException;
import io.swagger.exceptions.OrderNotFoundException;
import io.swagger.exceptions.TicketAlreadyReservedException;
import io.swagger.exceptions.TicketNotFoundException;
import io.swagger.model.*;
import io.swagger.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;


    @Autowired
    public ReservationServiceImpl(ReservationRepository IReservationRepository) {
        this.reservationRepository = IReservationRepository;
    }

    @Override
    public CreateEventResponse createEvent(CreateEventRequest request) {
        CreateEventResponse response = new CreateEventResponse();
        try {
            String eventId = reservationRepository.createEvent(
                    request.getEventName(),
                    request.getNumberOfSeats(),
                    request.getSeatPrice()
            );
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Event created successfully");
            response.setEventId(eventId);
            return response;
        } catch (Exception e) {
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to create event " + e.getMessage());
            return response;
        }
    }

    public ReserveTicketsResponse reserve(ReserveTicketsRequest request) {
        ReserveTicketsResponse response = new ReserveTicketsResponse();
        response.setOrderId(request.getOrderId());
        try {
            List<Ticket> tickets = reservationRepository.reserve(
                    request.getOrderId(),
                    request.getEventId(),
                    request.getTicketIds()
            );
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Tickets reserved successfully");
            response.setTickets(tickets);
            return response;
        } catch (EventNotFoundException | TicketNotFoundException e) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            return response;
        } catch (TicketAlreadyReservedException e) {
            response.setCode(HttpStatus.CONFLICT.value());
            response.setMessage(e.getMessage());
            return response;
        } catch (Exception e) {
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to reserve tickets: " + e.getMessage());
            return response;
        }
    }

    public ReleaseTicketsResponse release(String orderId) {
        ReleaseTicketsResponse response = new ReleaseTicketsResponse();
        try {
            reservationRepository.release(orderId);
            response.setCode(HttpStatus.OK.value());
            response.setMessage("Tickets released successfully");
            return response;
        } catch (OrderNotFoundException e) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setMessage(e.getMessage());
            return response;
        } catch (Exception e) {
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to release tickets: " + e.getMessage());
            return response;
        }
    }
}
