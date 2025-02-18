package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.BaseResponse;
import io.swagger.model.Ticket;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ReserveTicketsResponse
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-18T09:21:40.228845167Z[GMT]")


public class ReserveTicketsResponse extends BaseResponse  {
  @JsonProperty("orderId")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String orderId = null;

  @JsonProperty("tickets")
  @Valid
  private List<Ticket> tickets = null;

  public ReserveTicketsResponse orderId(String orderId) { 

    this.orderId = orderId;
    return this;
  }

  /**
   * Order ID associated with the reserved tickets
   * @return orderId
   **/
  
  @Schema(description = "Order ID associated with the reserved tickets")
  
  public String getOrderId() {  
    return orderId;
  }



  public void setOrderId(String orderId) { 
    this.orderId = orderId;
  }

  public ReserveTicketsResponse tickets(List<Ticket> tickets) { 

    this.tickets = tickets;
    return this;
  }

  public ReserveTicketsResponse addTicketsItem(Ticket ticketsItem) {
    if (this.tickets == null) {
      this.tickets = new ArrayList<Ticket>();
    }
    this.tickets.add(ticketsItem);
    return this;
  }

  /**
   * Get tickets
   * @return tickets
   **/
  
  @Schema(description = "")
  @Valid
  public List<Ticket> getTickets() {  
    return tickets;
  }



  public void setTickets(List<Ticket> tickets) { 
    this.tickets = tickets;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReserveTicketsResponse reserveTicketsResponse = (ReserveTicketsResponse) o;
    return Objects.equals(this.orderId, reserveTicketsResponse.orderId) &&
        Objects.equals(this.tickets, reserveTicketsResponse.tickets) &&
        super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, tickets, super.hashCode());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReserveTicketsResponse {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    tickets: ").append(toIndentedString(tickets)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
