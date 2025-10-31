package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una categoría de alojamiento.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

  private String code;

  private String simpleCode;

  private String accommodationType;

  private String group;

  private ContentDto description;
}
