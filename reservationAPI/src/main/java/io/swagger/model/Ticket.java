package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.TicketAvailability;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import org.openapitools.jackson.nullable.JsonNullable;
import io.swagger.configuration.NotUndefined;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Ticket
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-17T20:48:49.358959185Z[GMT]")


public class Ticket   {
  @JsonProperty("ticketId")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String ticketId = null;

  @JsonProperty("eventId")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String eventId = null;

  @JsonProperty("eventName")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String eventName = null;

  @JsonProperty("availability")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private TicketAvailability availability = null;

  @JsonProperty("price")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private Float price = null;


  public Ticket ticketId(String ticketId) { 

    this.ticketId = ticketId;
    return this;
  }

  /**
   * Unique ticket identifier
   * @return ticketId
   **/
  
  @Schema(description = "Unique ticket identifier")
  
  public String getTicketId() {  
    return ticketId;
  }



  public void setTicketId(String ticketId) { 
    this.ticketId = ticketId;
  }

  public Ticket eventId(String eventId) { 

    this.eventId = eventId;
    return this;
  }

  /**
   * Identifier for the associated event
   * @return eventId
   **/
  
  @Schema(description = "Identifier for the associated event")
  
  public String getEventId() {  
    return eventId;
  }



  public void setEventId(String eventId) { 
    this.eventId = eventId;
  }

  public Ticket eventName(String eventName) { 

    this.eventName = eventName;
    return this;
  }

  /**
   * Name of the event
   * @return eventName
   **/
  
  @Schema(description = "Name of the event")
  
  public String getEventName() {  
    return eventName;
  }



  public void setEventName(String eventName) { 
    this.eventName = eventName;
  }

  public Ticket availability(TicketAvailability availability) { 

    this.availability = availability;
    return this;
  }

  /**
   * Get availability
   * @return availability
   **/
  
  @Schema(description = "")
  
@Valid
  public TicketAvailability getAvailability() {  
    return availability;
  }



  public void setAvailability(TicketAvailability availability) { 
    this.availability = availability;
  }

  public Ticket price(Float price) { 

    this.price = price;
    return this;
  }

  /**
   * Ticket price
   * @return price
   **/
  
  @Schema(description = "Ticket price")
  
  public Float getPrice() {  
    return price;
  }



  public void setPrice(Float price) { 
    this.price = price;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ticket ticket = (Ticket) o;
    return Objects.equals(this.ticketId, ticket.ticketId) &&
        Objects.equals(this.eventId, ticket.eventId) &&
        Objects.equals(this.eventName, ticket.eventName) &&
        Objects.equals(this.availability, ticket.availability) &&
        Objects.equals(this.price, ticket.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, eventId, eventName, availability, price);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Ticket {\n");
    
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
    sb.append("    availability: ").append(toIndentedString(availability)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
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
