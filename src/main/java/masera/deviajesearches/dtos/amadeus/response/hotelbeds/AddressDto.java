package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.Data;

/**
 * DTO que representa la dirección de un hotel.
 */
@Data
public class AddressDto {
  private String content;
  private String street;
  private String number;
}
