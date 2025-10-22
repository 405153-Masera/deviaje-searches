package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una cadena hotelera.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChainDto {

  private String code;

  private ContentDto description;
}
