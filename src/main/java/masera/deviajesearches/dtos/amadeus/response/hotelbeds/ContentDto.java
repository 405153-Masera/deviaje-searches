package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa una descripción de varias respuestas de Hotelbeds.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {

  private String content;

  private String languageCode;
}
