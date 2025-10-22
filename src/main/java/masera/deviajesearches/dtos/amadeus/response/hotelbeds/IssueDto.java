package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un problema del hotel.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueDto {

  //Indica si hay una alternativa disponible para el problema.
  private Boolean alternative;

  //Tiempo desde el que el problema está vigente.
  private String dateFrom;

  private String dateTo;

  private ContentDto description;

  private String issueCode;

  private String issueType;

  private Integer order;
}
