package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un grupo de categorías.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryGroupDto {

  private String code;

  private ContentDto description;

  private ContentDto name;

  private Integer order;
}
