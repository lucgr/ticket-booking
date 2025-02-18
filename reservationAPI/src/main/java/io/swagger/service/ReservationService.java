package io.swagger.service;

import io.swagger.model.*;

public interface ReservationService {
    CreateEventResponse createEvent(CreateEventRequest request);

    ReserveTicketsResponse reserve(ReserveTicketsRequest request);

    ReleaseTicketsResponse release(String orderId);
}
