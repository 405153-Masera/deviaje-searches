package masera.deviajesearches.dtos.amadeus.response.hotelbeds.chains;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ContentDto;

/**
 * DTO para la respuesta de cadenas de hoteles de Hotelbeds.
 */
@Data
public class ChainsResponse {

  private List<ChainContent> chains;

  /**
   * Contenido de la cadena de hoteles.
   */
  @Data
  public static class ChainContent {

    private String code;

    private ContentDto description;
  }
}
