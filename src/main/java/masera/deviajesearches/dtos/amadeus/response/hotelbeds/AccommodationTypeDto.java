package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un tipo de alojamiento.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationTypeDto {

  private String code;

  private String typeDescription;
}
