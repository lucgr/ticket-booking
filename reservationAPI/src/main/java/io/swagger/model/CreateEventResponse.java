package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.model.BaseResponse;
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
 * Response after creating an event, returning the event ID.
 */
@Schema(description = "Response after creating an event, returning the event ID.")
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-17T20:48:49.358959185Z[GMT]")


public class CreateEventResponse   {
  @JsonProperty("baseResponse")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private BaseResponse baseResponse = null;

  @JsonProperty("eventId")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private String eventId = null;


  public CreateEventResponse baseResponse(BaseResponse baseResponse) { 

    this.baseResponse = baseResponse;
    return this;
  }

  /**
   * Get baseResponse
   * @return baseResponse
   **/
  
  @Schema(description = "")
  
@Valid
  public BaseResponse getBaseResponse() {  
    return baseResponse;
  }



  public void setBaseResponse(BaseResponse baseResponse) { 
    this.baseResponse = baseResponse;
  }

  public CreateEventResponse eventId(String eventId) { 

    this.eventId = eventId;
    return this;
  }

  /**
   * Unique identifier for the created event
   * @return eventId
   **/
  
  @Schema(description = "Unique identifier for the created event")
  
  public String getEventId() {  
    return eventId;
  }



  public void setEventId(String eventId) { 
    this.eventId = eventId;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateEventResponse createEventResponse = (CreateEventResponse) o;
    return Objects.equals(this.baseResponse, createEventResponse.baseResponse) &&
        Objects.equals(this.eventId, createEventResponse.eventId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseResponse, eventId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateEventResponse {\n");
    
    sb.append("    baseResponse: ").append(toIndentedString(baseResponse)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
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
