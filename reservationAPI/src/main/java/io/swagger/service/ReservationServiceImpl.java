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
        try {
            String eventId = reservationRepository.createEvent(
                    request.getEventName(),
                    request.getNumberOfSeats(),
                    request.getSeatPrice()
            );
            return new CreateEventResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.OK.value())
                                    .message("Event created successfully")
                                    .status("success")
                    ).eventId(eventId);
        } catch (Exception e) {
            return new CreateEventResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .message("Failed to reserve tickets " + e.getMessage())
                                    .status("error")
                    );
        }
    }

    public ReserveTicketsResponse reserve(ReserveTicketsRequest request) {
        try {
            List<Ticket> tickets = reservationRepository.reserve(
                    request.getOrderId(),
                    request.getEventId(),
                    request.getTicketIds()
            );
            return new ReserveTicketsResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.OK.value())
                                    .message("Tickets reserved successfully")
                                    .status("success")
                    ).tickets(tickets);
        } catch (EventNotFoundException e) {
            return new ReserveTicketsResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.NOT_FOUND.value())
                                    .message("Event not found")
                                    .status("error")
                    );
        } catch (TicketNotFoundException e) {
            return new ReserveTicketsResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.NOT_FOUND.value())
                                    .message("Ticket(s) not found")
                                    .status("error")
                    );
        } catch (TicketAlreadyReservedException e) {
            return new ReserveTicketsResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.CONFLICT.value())
                                    .message(e.getMessage())
                                    .status("error")
                    );
        } catch (Exception e) {
            return new ReserveTicketsResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .message("Failed to reserve tickets: " + e.getMessage())
                                    .status("error")
                    );
        }
    }

    public ReleaseTicketsResponse release(String orderId) {
        try {
            reservationRepository.release(orderId);
            return new ReleaseTicketsResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.OK.value())
                                    .message("Tickets released successfully")
                                    .status("success")
                    );
        } catch (OrderNotFoundException e) {
            return new ReleaseTicketsResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.NOT_FOUND.value())
                                    .message("Order not found")
                                    .status("error")
                    );
        } catch (Exception e) {
            return new ReleaseTicketsResponse()
                    .baseResponse(
                            new BaseResponse()
                                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .message("Failed to release tickets")
                                    .status("error")
                    );
        }
    }
}
