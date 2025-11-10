package masera.deviajesearches.dtos.amadeus.response.hotelbeds.terminals;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ContentDto;

/**
 * DTO para la respuesta de terminales de Hotelbeds.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TerminalsResponse {

  private List<TerminalData> terminals;

  /**
   * Representa una terminal.
   */
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TerminalData {

    private String code;

    private String type;

    private String country;

    private ContentDto name;

    private ContentDto description;
  }
}