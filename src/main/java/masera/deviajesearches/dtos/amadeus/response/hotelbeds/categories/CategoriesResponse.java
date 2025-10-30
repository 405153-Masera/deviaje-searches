package masera.deviajesearches.dtos.amadeus.response.hotelbeds.categories;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.ContentDto;

/**
 * DTO para la respuesta de categorías de alojamiento de Hotelbeds.
 */
@Data
public class CategoriesResponse {

  private List<CategoryContent> categories;

  /**
   * Contenido del tipo de alojamiento.
   */
  @Data
  public static class CategoryContent {

    private String code;

    private String simpleCode;

    private String accommodationType;

    private String group;

    private ContentDto description;
  }
}
