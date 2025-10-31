package masera.deviajesearches.dtos.amadeus.response.hotelbeds.chains;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ChainDto;

/**
 * DTO para la respuesta de cadenas de hoteles de Hotelbeds.
 */
@Data
public class ChainsResponse {

  private List<ChainDto> chains;
}
