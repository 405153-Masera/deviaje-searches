package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un régimen de alojamiento (alimentos).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

  private String code;

  private ContentDto description;
}
