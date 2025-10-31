package masera.deviajesearches.dtos.amadeus.response.hotelbeds.boards;

import java.util.List;
import lombok.Data;
import masera.deviajesearches.dtos.amadeus.response.hotelbeds.BoardDto;

/**
 * DTO para la respuesta de regímenes de alimentos de Hotelbeds.
 */
@Data
public class BoardsResponse {

  private List<BoardDto> boards;
}
