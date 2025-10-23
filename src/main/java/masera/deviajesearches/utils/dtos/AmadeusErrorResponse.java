package masera.deviajesearches.utils.dtos;

import java.util.List;
import lombok.Data;

/**
 * DTO para manejar respuestas de error de Amadeus.
 */
@Data
public class AmadeusErrorResponse {

  private List<AmadeusError> errors;
}
