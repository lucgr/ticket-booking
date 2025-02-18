package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ReserveTicketsRequest
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-18T09:21:40.228845167Z[GMT]")


public class ReserveTicketsRequest   {
  @JsonProperty("orderId")

  private String orderId = null;

  @JsonProperty("userId")

  private String userId = null;

  @JsonProperty("eventId")

  private String eventId = null;

  @JsonProperty("ticketIds")
  @Valid
  private List<String> ticketIds = new ArrayList<String>();

  public ReserveTicketsRequest orderId(String orderId) { 

    this.orderId = orderId;
    return this;
  }

  /**
   * Unique order identifier
   * @return orderId
   **/
  
  @Schema(required = true, description = "Unique order identifier")
  
  @NotNull
  public String getOrderId() {  
    return orderId;
  }



  public void setOrderId(String orderId) { 

    this.orderId = orderId;
  }

  public ReserveTicketsRequest userId(String userId) { 

    this.userId = userId;
    return this;
  }

  /**
   * Identifier for the user reserving the tickets
   * @return userId
   **/
  
  @Schema(required = true, description = "Identifier for the user reserving the tickets")
  
  @NotNull
  public String getUserId() {  
    return userId;
  }



  public void setUserId(String userId) { 

    this.userId = userId;
  }

  public ReserveTicketsRequest eventId(String eventId) { 

    this.eventId = eventId;
    return this;
  }

  /**
   * Identifier for the event
   * @return eventId
   **/
  
  @Schema(required = true, description = "Identifier for the event")
  
  @NotNull
  public String getEventId() {  
    return eventId;
  }



  public void setEventId(String eventId) { 

    this.eventId = eventId;
  }

  public ReserveTicketsRequest ticketIds(List<String> ticketIds) { 

    this.ticketIds = ticketIds;
    return this;
  }

  public ReserveTicketsRequest addTicketIdsItem(String ticketIdsItem) {
    this.ticketIds.add(ticketIdsItem);
    return this;
  }

  /**
   * List of ticket IDs to be reserved
   * @return ticketIds
   **/
  
  @Schema(required = true, description = "List of ticket IDs to be reserved")
  
  @NotNull
  public List<String> getTicketIds() {  
    return ticketIds;
  }



  public void setTicketIds(List<String> ticketIds) { 

    this.ticketIds = ticketIds;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReserveTicketsRequest reserveTicketsRequest = (ReserveTicketsRequest) o;
    return Objects.equals(this.orderId, reserveTicketsRequest.orderId) &&
        Objects.equals(this.userId, reserveTicketsRequest.userId) &&
        Objects.equals(this.eventId, reserveTicketsRequest.eventId) &&
        Objects.equals(this.ticketIds, reserveTicketsRequest.ticketIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, userId, eventId, ticketIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReserveTicketsRequest {\n");
    
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    ticketIds: ").append(toIndentedString(ticketIds)).append("\n");
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
