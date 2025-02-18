package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.ReleaseTicketsResponse;
import io.swagger.model.ReserveTicketsRequest;
import io.swagger.model.ReserveTicketsResponse;
import io.swagger.service.ReservationService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-17T20:48:49.358959185Z[GMT]")
@RestController
public class ReservationsApiController implements ReservationsApi {

    private static final Logger log = LoggerFactory.getLogger(ReservationsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final ReservationService reservationService;

    @org.springframework.beans.factory.annotation.Autowired
    public ReservationsApiController(ObjectMapper objectMapper, HttpServletRequest request, ReservationService reservationService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.reservationService = reservationService;
    }

    public ResponseEntity<ReleaseTicketsResponse> releaseTickets(@Parameter(in = ParameterIn.PATH, description = "The order ID whose tickets should be released", required = true, schema = @Schema()) @PathVariable("orderId") String orderId
    ) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                ReleaseTicketsResponse response = reservationService.release(orderId);
                return new ResponseEntity<ReleaseTicketsResponse>(response, HttpStatus.valueOf(response.getBaseResponse().getCode()));
            } catch (Exception e) {
                log.error("An error occurred while releasing tickets", e);
                return new ResponseEntity<ReleaseTicketsResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<ReleaseTicketsResponse>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ReserveTicketsResponse> reserveTickets(@Parameter(description = "", required = true) @Valid @RequestBody ReserveTicketsRequest body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                ReserveTicketsResponse response = reservationService.reserve(body);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (Exception e) {
                log.error("An error occurred while reserving tickets", e);
                return new ResponseEntity<ReserveTicketsResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<ReserveTicketsResponse>(HttpStatus.BAD_REQUEST);
    }

}
