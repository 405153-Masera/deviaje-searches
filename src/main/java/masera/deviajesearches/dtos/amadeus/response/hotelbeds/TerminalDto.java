package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.Data;

/**
 * DTO que representa la terminal cercana de un hotel.
 */
@Data
public class TerminalDto {
  private String terminalCode;
  private Integer distance;
}
