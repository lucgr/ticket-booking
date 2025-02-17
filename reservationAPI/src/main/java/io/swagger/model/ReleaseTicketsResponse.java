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
 * Response after releasing tickets.
 */
@Schema(description = "Response after releasing tickets.")
@Validated
@NotUndefined
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2025-02-17T20:48:49.358959185Z[GMT]")


public class ReleaseTicketsResponse   {
  @JsonProperty("baseResponse")

  @JsonInclude(JsonInclude.Include.NON_ABSENT)  // Exclude from JSON if absent
  @JsonSetter(nulls = Nulls.FAIL)    // FAIL setting if the value is null
  private BaseResponse baseResponse = null;


  public ReleaseTicketsResponse baseResponse(BaseResponse baseResponse) { 

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

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReleaseTicketsResponse releaseTicketsResponse = (ReleaseTicketsResponse) o;
    return Objects.equals(this.baseResponse, releaseTicketsResponse.baseResponse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseResponse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReleaseTicketsResponse {\n");
    
    sb.append("    baseResponse: ").append(toIndentedString(baseResponse)).append("\n");
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
