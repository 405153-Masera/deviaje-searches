package masera.deviajesearches.dtos.amadeus.response.hotelbeds.accommodations;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.AccommodationTypeDto;

/**
 * DTO para la respuesta de tipos de alojamiento de Hotelbeds.
 */
@Data
public class AccommodationResponse {

  List<AccommodationTypeDto> accommodations;
}
