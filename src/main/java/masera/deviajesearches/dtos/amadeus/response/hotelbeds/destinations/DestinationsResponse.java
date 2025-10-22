package masera.deviajesearches.dtos.amadeus.response.hotelbeds.destinations;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ContentDto;

/**
 * DTO que representa la respuesta de la API de Hotelbeds para obtener destinos.
 */
@Data
public class DestinationsResponse {

  private List<DestinationContent> destinations;

  /**
   * Clase interna que representa los datos de los destinos.
   */
  @Data
  public static class DestinationContent {

    private String code;

    private ContentDto name;

    private String countryCode;

    private String isoCode;
  }
}
