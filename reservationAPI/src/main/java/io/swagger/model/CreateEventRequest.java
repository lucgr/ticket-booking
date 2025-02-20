package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CreateEventRequest
 */
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-18T09:21:40.228845167Z[GMT]")


public class CreateEventRequest   {
  @JsonProperty("eventName")

  private String eventName = null;

  @JsonProperty("numberOfSeats")

  private Integer numberOfSeats = null;

  @JsonProperty("seatPrice")

  private Float seatPrice = null;


  public CreateEventRequest eventName(String eventName) { 

    this.eventName = eventName;
    return this;
  }

  /**
   * Name of the event
   * @return eventName
   **/
  
  @Schema(required = true, description = "Name of the event")
  
  @NotNull
  public String getEventName() {  
    return eventName;
  }



  public void setEventName(String eventName) { 

    this.eventName = eventName;
  }

  public CreateEventRequest numberOfSeats(Integer numberOfSeats) { 

    this.numberOfSeats = numberOfSeats;
    return this;
  }

  /**
   * Total number of seats available
   * @return numberOfSeats
   **/
  
  @Schema(example = "100", required = true, description = "Total number of seats available")
  
  @NotNull
  public Integer getNumberOfSeats() {  
    return numberOfSeats;
  }



  public void setNumberOfSeats(Integer numberOfSeats) { 

    this.numberOfSeats = numberOfSeats;
  }

  public CreateEventRequest seatPrice(Float seatPrice) { 

    this.seatPrice = seatPrice;
    return this;
  }

  /**
   * Price of each seat
   * @return seatPrice
   **/
  
  @Schema(example = "100.15", required = true, description = "Price of each seat")
  
  @NotNull
  public Float getSeatPrice() {  
    return seatPrice;
  }



  public void setSeatPrice(Float seatPrice) { 

    this.seatPrice = seatPrice;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateEventRequest createEventRequest = (CreateEventRequest) o;
    return Objects.equals(this.eventName, createEventRequest.eventName) &&
        Objects.equals(this.numberOfSeats, createEventRequest.numberOfSeats) &&
        Objects.equals(this.seatPrice, createEventRequest.seatPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventName, numberOfSeats, seatPrice);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateEventRequest {\n");
    
    sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
    sb.append("    numberOfSeats: ").append(toIndentedString(numberOfSeats)).append("\n");
    sb.append("    seatPrice: ").append(toIndentedString(seatPrice)).append("\n");
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
