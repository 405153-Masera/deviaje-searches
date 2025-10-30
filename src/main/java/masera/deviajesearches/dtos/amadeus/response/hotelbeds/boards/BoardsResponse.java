package masera.deviajesearches.dtos.amadeus.response.hotelbeds.boards;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ContentDto;

/**
 * DTO para la respuesta de regímenes de alimentos de Hotelbeds.
 */
@Data
public class BoardsResponse {

  private List<BoardContent> boards;

  /**
   * Contenido del tipo de alojamiento.
   */
  @Data
  public static class BoardContent {

    private String code;

    private ContentDto description;

    private String multiLingualCode;
  }
}
