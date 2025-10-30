package masera.deviajesearches.dtos.amadeus.response.hotelbeds.facilities;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ContentDto;

/**
 * DTO que representa la respuesta de la API de Hotelbeds para obtener grupos de instalaciones.
 */
@Data
public class FacilityGroupsResponse {

  private List<FacilityGroupContent> facilityGroups;

  /**
   * Clase interna que representa los grupos de instalaciones.
   */
  @Data
  public static class FacilityGroupContent {

    private Integer code;

    private ContentDto description;
  }
}
