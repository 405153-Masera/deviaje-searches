package masera.deviajesearches.dtos.amadeus.response.hotelbeds.facilities;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ContentDto;

/**
 * DTO que representa la respuesta de la API de Hotelbeds para obtener instalaciones.
 */
@Data
public class FacilitiesResponse {

  private List<FacilityContent> facilities;

  /**
   * Clase interna que representa los datos de las instalaciones.
   */
  @Data
  public static class FacilityContent {

    private Integer code;

    private Integer facilityGroupCode;

    private Integer facilityTypologyCode;

    private ContentDto description;
  }
}