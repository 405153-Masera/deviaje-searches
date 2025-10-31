package masera.deviajesearches.dtos.amadeus.response;

import lombok.Data;

/**
 * DTO que representa una ciudad y país.
 */
@Data
public class IataResponse {

  private String iata;

  private String name;

  private String city;

  private String country;
}