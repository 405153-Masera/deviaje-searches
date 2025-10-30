package masera.deviajesearches.dtos.amadeus.response.hotelbeds.accommodations;

import java.util.List;
import lombok.Data;

/**
 * DTO para la respuesta de tipos de alojamiento de Hotelbeds.
 */
@Data
public class AccommodationResponse {

  List<AccommodationContent> accommodations;

  /**
   * Contenido del tipo de alojamiento.
   */
  @Data
  public static class AccommodationContent {

    private String code;

    private String typeDescription;
  }
}
