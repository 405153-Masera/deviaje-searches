package masera.deviajesearches.dtos.amadeus.response.hotelbeds;


import java.util.List;
import lombok.Data;

/**
 * DTO que representa la respuesta de la API de Hotelbeds para obtener países.
 */
@Data
public class CountriesResponse {

  private List<CountryContent> countries;
  private AuditData auditData;

  /**
   * Clase interna que representa los datos de los países.
   */
  @Data
  public static class CountryContent {
    private String code;
    private ContentDto description;
    private List<StateContent> states;
  }

  /**
   * Clase interna que representa los datos de los estados.
   */
  @Data
  public static class StateContent {
    private String code;
    private ContentDto name;
  }
}
