package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa las coordenadas de un hotel.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates {

  private Double longitude;

  private Double latitude;
}
