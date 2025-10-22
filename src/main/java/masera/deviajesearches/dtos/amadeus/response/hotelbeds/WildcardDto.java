package masera.deviajesearches.dtos.amadeus.response.hotelbeds;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un comodín de habitación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WildcardDto {

  private String roomType;

  private String roomCode;

  private String characteristicCode;

  private ContentDto hotelRoomDescription;
}
