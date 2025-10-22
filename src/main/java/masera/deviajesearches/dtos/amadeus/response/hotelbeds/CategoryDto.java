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

  private String accommodationType;

  private String code;

  private ContentDto description;

  private String group;

  private Integer simpleCode;
}
