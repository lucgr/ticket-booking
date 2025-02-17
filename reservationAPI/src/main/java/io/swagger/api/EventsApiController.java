package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.CreateEventRequest;
import io.swagger.model.CreateEventResponse;
import io.swagger.service.ReservationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-17T20:48:49.358959185Z[GMT]")
@RestController
public class EventsApiController implements EventsApi {

    private static final Logger log = LoggerFactory.getLogger(EventsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final ReservationService reservationService;

    @org.springframework.beans.factory.annotation.Autowired
    public EventsApiController(ObjectMapper objectMapper, HttpServletRequest request, ReservationService reservationService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.reservationService = reservationService;
    }

    public ResponseEntity<CreateEventResponse> createEvent(@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody CreateEventRequest body
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                CreateEventResponse response = reservationService.createEvent(body);
                return new ResponseEntity<CreateEventResponse>(response, HttpStatus.valueOf(response.getBaseResponse().getCode()));
            } catch (Exception e) {
                log.error("An error occurred while releasing tickets", e);
                return new ResponseEntity<CreateEventResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<CreateEventResponse>(HttpStatus.BAD_REQUEST);
    }

}
