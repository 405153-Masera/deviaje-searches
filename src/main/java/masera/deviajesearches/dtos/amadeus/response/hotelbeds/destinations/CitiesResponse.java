package masera.deviajesearches.dtos.amadeus.response.hotelbeds.destinations;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.CityDto;

/**
 * Representa la respuesta de la api amadeus.
 */
@Data
public class CitiesResponse {

  private List<CityDto> data;
}
