package masera.deviajesearches.dtos.amadeus.response.hotelbeds.categories;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.CategoryDto;

/**
 * DTO para la respuesta de categorías de alojamiento de Hotelbeds.
 */
@Data
public class CategoriesResponse {

  private List<CategoryDto> categories;
}
